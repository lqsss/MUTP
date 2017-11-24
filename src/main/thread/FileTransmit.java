package main.thread;

import main.MUTPInterface.RecvACKHandleInterface;
import main.common.DataPacket;
import main.common.DataPacketFactory;
import main.common.MutpConst;
import main.utils.PropertiesReader;
import org.apache.log4j.Logger;

import java.io.*;
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

    public FileTransmit(RecvACKHandleInterface impl) {
        this.impl = impl;
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
            allPackets.put(count, buf);
            count += bufSize;
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
            int lastSentByte = impl.getLastSentedByte();
            for (int i = 0; i < sendSizeLimit; i++) {
                DataPacket dpk = DataPacketFactory.getInstance(MutpConst.DATA_ONLY);

                if (lastSentByte == -1) {
                    //第一次发送
                    dpk.setBuf(allPackets.get(2));
                    dpk.getHeader().setSeqNum(2);
                }else{
                    //假设发送seq = 3 2是第一个字节序号，2、3、4，下一个发5-----
                    dpk.setBuf(allPackets.get(lastSentByte+1));
                    dpk.getHeader().setSeqNum(lastSentByte+1);
                }
                
                
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("readFileInMap error!");
            e.printStackTrace();
        }
    }
}
