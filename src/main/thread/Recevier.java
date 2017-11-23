package main.thread;

import main.MUTPInterface.ReceiverHandleInterface;
import main.MUTPInterface.RecvACKHandleInterface;
import main.MUTPInterface.impl.RecvACKHandleImpl;
import main.MUTPInterface.impl.RecvHandleImpl;
import main.common.ConnectState;
import main.common.DataPacket;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 接收器线程
 * Created by liqiushi on 2017/11/21.
 */
public class Recevier implements Runnable {
    
    private DatagramSocket srvSocket;
    private InetSocketAddress dstSocketAddr;
    private ReceiverHandleInterface recvHandle = new RecvHandleImpl();
    private Logger logger = Logger.getLogger(Recevier.class);

    public Recevier(DatagramSocket srvSocket, InetSocketAddress dstSocketAddr) {
        this.srvSocket = srvSocket;
        this.dstSocketAddr = dstSocketAddr;
    }
    
    public void handle(DataPacket dataPacket,Recevier recevier) throws IOException {
        recvHandle.handle(dataPacket,recevier);
    }
    
    public DatagramSocket getSrvSocket() {
        return srvSocket;
    }

    public void setSrvSocket(DatagramSocket srvSocket) {
        this.srvSocket = srvSocket;
    }

    public InetSocketAddress getDstSocketAddr() {
        return dstSocketAddr;
    }

    public void setDstSocketAddr(InetSocketAddress dstSocketAddr) {
        this.dstSocketAddr = dstSocketAddr;
    }
    @Override
    public void run() {
        logger.info("server thread has started!");
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        while (true) {
            byte[] buf = new byte[1024 * 2];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            try {
                srvSocket.receive(dp);
                bais = new ByteArrayInputStream(buf);
                ois = new ObjectInputStream(bais);
                DataPacket dataPacket = (DataPacket) ois.readObject();
                handle(dataPacket,this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                logger.info("读取对象失败！");
                e.printStackTrace();
            }
        }
    }
}
