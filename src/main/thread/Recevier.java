package main.thread;

import main.MUTPInterface.ReceiverHandleInterface;
import main.MUTPInterface.impl.RecvHandleImpl;
import main.common.DataPacket;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.TreeMap;

/**
 * 接收器线程
 * Created by liqiushi on 2017/11/21.
 */
public class Recevier implements Runnable {
    
    private DatagramSocket srvSocket;
    private InetSocketAddress dstSocketAddr;
    private ReceiverHandleInterface recvHandle = new RecvHandleImpl();
    private Map<Integer,byte[]> tmpList = new TreeMap<>();
    private int expectSeqnum =2;


    private int windowSize;

    private BufferedOutputStream bos = null;
    private Logger logger = Logger.getLogger(Recevier.class);

    public Recevier(DatagramSocket srvSocket, InetSocketAddress dstSocketAddr) throws FileNotFoundException {
        this.srvSocket = srvSocket;
        this.dstSocketAddr = dstSocketAddr;
        bos = new BufferedOutputStream(new FileOutputStream("E:/8.mp3"));
    }


    public BufferedOutputStream getBos() {
        return bos;
    }

    public void setBos(BufferedOutputStream bos) {
        this.bos = bos;
    }

    public Map<Integer, byte[]> getTmpList() {
        return tmpList;
    }

    public void setTmpList(Map<Integer, byte[]> tmpList) {
        this.tmpList = tmpList;
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

    public int getExpectSeqnum() {
        return expectSeqnum;
    }

    public void setExpectSeqnum(int expectSeqnum) {
        this.expectSeqnum = expectSeqnum;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public void run() {
        logger.info("server thread has started!");
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        while (true) {
            byte[] buf = new byte[10240* 2];
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
