package main.thread;

import main.MUTPInterface.RecvACKHandleInterface;
import main.MUTPInterface.impl.RecvACKHandleImpl;
import main.common.ConnectState;
import main.common.DataPacket;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqiushi on 2017/11/23.
 */
public class Recvtor implements Runnable{
    private DatagramSocket cliSocket;
    private InetSocketAddress dstSocketAddr;
    private ConnectState connectState;

    private RecvACKHandleInterface recvImpl = new RecvACKHandleImpl(); 
    
/*    private M<Integer,byte[]> = new ArrayList*/

    private Logger logger = Logger.getLogger(Recvtor.class);

    public Recvtor(DatagramSocket cliSocket, InetSocketAddress dstSocketAddr, ConnectState connectState) {
        this.cliSocket = cliSocket;
        this.dstSocketAddr = dstSocketAddr;
        this.connectState = connectState;
    }
  


    public ConnectState getConnectState() {
        return connectState;
    }

    public void setConnectState(ConnectState connectState) {
        this.connectState = connectState;
    }


    public void handle(DataPacket dataPacket){
        recvImpl.handleAsSYNACK(dataPacket,this);
    }

    @Override
    public void run() {
        //client接受
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        while(true){
            byte[] buf = new byte[1024*2];
            DatagramPacket dp = new DatagramPacket(buf,buf.length);
            try {
                cliSocket.receive(dp);
                bais = new ByteArrayInputStream(buf);
                ois = new ObjectInputStream(bais);
                DataPacket dataPacket = (DataPacket) ois.readObject();
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
