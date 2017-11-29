package main.thread;

import main.MUTPInterface.RecvACKHandleInterface;
import main.common.DataPacket;
import main.common.DataPacketFactory;
import main.common.MutpConst;
import main.utils.PacketUtil;
import main.utils.PropertiesReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liqiushi on 2017/11/24.
 */
public class FileTransmit implements Runnable {
    //滑动窗口
/*    1. 发送的文件分成一个一个有相应序号对应的小包
    2. 根据对面的ACK反应情况，看是否需要重传*/
    private Logger logger = Logger.getLogger(FileTransmit.class);

    //    private int windowSizeMaxium = 5;
    private int lastSend = 5;
    private RecvACKHandleInterface impl = null;
    DatagramSocket cliSocket;
    InetSocketAddress dstSocketAddr;

    public FileTransmit(RecvACKHandleInterface impl, DatagramSocket cliSocket, InetSocketAddress dstSocketAddr) {
        this.impl = impl;
        this.cliSocket = cliSocket;
        this.dstSocketAddr = dstSocketAddr;
    }

    /**
     * 应用层所有传输的数据
     *
     * @param accessFile
     * @return
     * @throws IOException
     */
    public Map<Integer, byte[]> readFileInMap(RandomAccessFile accessFile) throws IOException {
        Map<Integer, byte[]> allPackets = new HashMap<>();
        //读取文件流
        int readSize = -1;
        int count = 2;
        int bufSize = Integer.parseInt(PropertiesReader.getInstance().getValue("bufSize"));
        byte[] buf = new byte[bufSize];
        while ((readSize = accessFile.read(buf, 0, buf.length)) != -1) {
            byte[] tmpBuf = new byte[readSize];
            System.arraycopy(buf, 0, tmpBuf, 0, readSize);
            allPackets.put(count, tmpBuf);
            count += readSize;
        }
        return allPackets;
    }


    @Override
    public void run() {
        //读取文件到一个Map里,来模拟
        //分为4段 1 已经被ACK的,则移除掉 2 在窗口中，且已发送但并未ACKED 3 在窗口中，将要发送的
        //4 窗口外，不允许发送的
        Map<Integer, byte[]> allPackets = null;
        Map<Integer, byte[]> slidingWindow = null;
        try {
            RandomAccessFile accessFile = new RandomAccessFile(PropertiesReader.getInstance().getValue("uploadFile"), "r");
            allPackets = readFileInMap(accessFile);
            //发送
            int sendSizeLimit = impl.getSendSizeLimit();

            for (int i = 0; i < sendSizeLimit; i++) {
                int lastSentByte = impl.getLastSentedByte();
                DataPacket dpk = DataPacketFactory.getInstance(MutpConst.DATA_ONLY);
                if (lastSentByte == -1) {
                    //第一次发送
                    dpk.setBuf(allPackets.get(2));
                    dpk.getHeader().setSeqNum(2);
                    impl.setLastSentedByte(2 + dpk.getBuf().length - 1);
                } else {
                    //假设发送seq = 3 3是第一个字节序号，3、4，下一个发5-----
                    dpk.setBuf(allPackets.get(lastSentByte + 1));
                    if (dpk.getBuf() == null) {
                        logger.info("null   "+lastSentByte + 1);
                        return;
                    }
                    dpk.getHeader().setSeqNum(impl.getLastSentedByte() + 1);
                    impl.setLastSentedByte(lastSentByte + dpk.getBuf().length);
                }
                PacketUtil.sendPackt(cliSocket, dstSocketAddr, dpk);
                //Todo 定时任务
                new TimerCheckout(impl,dpk,cliSocket,dstSocketAddr).goTask();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("readFileInMap error!");
            e.printStackTrace();
        }
    }
}
