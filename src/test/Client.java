package test;

import main.common.DataPacket;
import main.common.DataPacketHeader;
import main.common.MutpConst;
import main.utils.PropertiesReader;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by liqiushi on 2017/11/22.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        String dstPort = MutpConst.dstPort;
        String dstHost = MutpConst.dstHost;
        String srcPort = MutpConst.srcPort;
        String srcHost = MutpConst.srcHost;
        InetSocketAddress clientSocketAddr = new InetSocketAddress(srcHost,Integer.parseInt(srcPort));
        InetSocketAddress serverSocketAddr = new InetSocketAddress(dstHost,Integer.parseInt(dstPort));
        try {
            DatagramSocket socket = new DatagramSocket(clientSocketAddr);
    /*      private boolean ACK;
            private boolean SYN;
            private int ackNum;
            private int seqNum;*/
            DataPacketHeader dph = new DataPacketHeader(true,true,1,12);
            //DataPacket dp = new DataPacket(dph);
            ByteArrayOutputStream baos = null;
            ObjectOutputStream dos = null;
            int count = 0;
           for(int i = 0;i<3;i++){
               baos = new ByteArrayOutputStream();
               dos = new ObjectOutputStream(baos);
               count++;
               
               dph.setAckNum(count);
               dos.writeObject(dph);
               dos.flush();
               byte buf[] = baos.toByteArray();
               DatagramPacket dpk = new DatagramPacket(buf,buf.length,serverSocketAddr);
               System.out.println("buffer length " + buf.length);
               socket.send(dpk);
               baos.close();
               dos.close();
           }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
}
