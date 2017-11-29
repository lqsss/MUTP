package main.thread;

import main.MUTPInterface.RecvACKHandleInterface;
import main.MUTPInterface.impl.RecvACKHandleImpl;
import main.common.ConnectState;
import main.common.DataPacket;
import main.utils.PacketUtil;
import main.utils.PropertiesReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created by liqiushi on 2017/11/23.
 */
public class Recvtor implements Runnable {

    private DatagramSocket cliSocket;
    private InetSocketAddress dstSocketAddr;
    private ConnectState connectState;
    private RecvACKHandleInterface recvImpl = new RecvACKHandleImpl();
    private Map<Integer, byte[]> allPackets;
/*    private M<Integer,byte[]> = new ArrayList*/

    private Logger logger = Logger.getLogger(Recvtor.class);

    public Recvtor(DatagramSocket cliSocket, InetSocketAddress dstSocketAddr, ConnectState connectState) {
        this.cliSocket = cliSocket;
        this.dstSocketAddr = dstSocketAddr;
        this.connectState = connectState;
        allPackets = createAllPackets();
    }

    public Map<Integer, byte[]> getAllPackets() {
        return allPackets;
    }

    public void setAllPackets(Map<Integer, byte[]> allPackets) {
        this.allPackets = allPackets;
    }


    public Map<Integer, byte[]> createAllPackets() {
        Map<Integer, byte[]> allPackets = null;

        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile(PropertiesReader.getInstance().getValue("uploadFile"), "r");
            allPackets = PacketUtil.readFileInMap(accessFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allPackets;
    }


    public ConnectState getConnectState() {
        return connectState;
    }

    public void setConnectState(ConnectState connectState) {
        this.connectState = connectState;
    }

    public DatagramSocket getCliSocket() {
        return cliSocket;
    }

    public void setCliSocket(DatagramSocket cliSocket) {
        this.cliSocket = cliSocket;
    }

    public InetSocketAddress getDstSocketAddr() {
        return dstSocketAddr;
    }

    public void setDstSocketAddr(InetSocketAddress dstSocketAddr) {
        this.dstSocketAddr = dstSocketAddr;
    }


    public void handle(DataPacket dataPacket) {
        recvImpl.handleAsSYNACK(dataPacket, this);
    }

    @Override
    public void run() {
        //client接受
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;


        while (true) {
            byte[] buf = new byte[10240 * 2];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            try {
                cliSocket.receive(dp);
                bais = new ByteArrayInputStream(buf);
                ois = new ObjectInputStream(bais);
                DataPacket dataPacket = (DataPacket) ois.readObject();
                if (dataPacket == null) {
                    logger.info("null");
                }
                handle(dataPacket);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                logger.info("读取对象失败！");
                e.printStackTrace();
            }
        }
    }
}
