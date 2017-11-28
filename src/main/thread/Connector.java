package main.thread;

import main.common.ConnectState;
import main.common.DataPacket;
import main.common.DataPacketFactory;
import main.common.MutpConst;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * 连接器
 * 完成牵手连接过程，负责发送方这边响应
 * Created by liqiushi on 2017/11/23.
 */
public class Connector implements Runnable {
    private DatagramSocket cliSocket;
    private InetSocketAddress dstSocketAddr;
    private ConnectState connectState;
    
    private Logger logger = Logger.getLogger(Connector.class);

    public Connector(DatagramSocket cliSocket, InetSocketAddress dstSocketAddr, ConnectState connectState) {
        this.cliSocket = cliSocket;
        this.dstSocketAddr = dstSocketAddr;
        this.connectState = connectState;
    }

    public void sendPackt(DatagramSocket srcSocket, InetSocketAddress dstSocketAddr, DataPacket dataPacket) throws IOException {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new ObjectOutputStream(baos);
            dos.writeUnshared(dataPacket);
            dos.flush();
            byte onceBuf[] = baos.toByteArray();
            DatagramPacket dpk = new DatagramPacket(onceBuf, onceBuf.length, dstSocketAddr);
            srcSocket.send(dpk);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            baos.close();
            dos.close();
        }
    }

    @Override
    public void run() {
        DatagramPacket sendDpk = null;
        DatagramPacket recvDpk = null;

        while (!connectState.isConnected()) {
            //首先发送只有SYN的包
            DataPacket dataPacket = DataPacketFactory.getInstance(MutpConst.SYN_ONLY);
            try {
                sendPackt(cliSocket, dstSocketAddr, dataPacket);
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.info("线程中断!");
            } catch (IOException e) {
                logger.info("SYN包发送失败!");
                e.printStackTrace();
            }
            if (!connectState.isSynAck()) {
                logger.info("没有收到SYN ACK包");
                logger.info("继续发送SYN包");
                continue;
            }
            dataPacket = DataPacketFactory.getInstance(MutpConst.ACK_ONLY);
            try {
                sendPackt(cliSocket, dstSocketAddr, dataPacket);
                Thread.sleep(2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("确认连接成功！");
        //传输文件的线程
     
    }
}
