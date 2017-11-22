package main.client;

import main.common.DataPacket;
import main.common.DataPacketHeader;
import main.common.mutpConst;
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
        String dstPort = mutpConst.dstPort;
        String dstHost = mutpConst.dstHost;
        String srcPort = mutpConst.srcPort;
        String srcHost = mutpConst.srcHost;
        InetSocketAddress clientSocketAddr = new InetSocketAddress(srcHost,Integer.parseInt(srcPort));
        InetSocketAddress serverSocketAddr = new InetSocketAddress(dstHost,Integer.parseInt(dstPort));
        try {
            DatagramSocket socket = new DatagramSocket(clientSocketAddr);
    /*      private boolean ACK;
            private boolean SYN;
            private int ackNum;
            private int seqNum;*/
            DataPacketHeader dph = new DataPacketHeader(true,true,12,1);
            DataPacket dp = new DataPacket(dph);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream dos = new ObjectOutputStream(baos);
            dos.writeObject(dp);
            dos.flush();
            
            byte buf[] = baos.toByteArray();
            DatagramPacket dpk = new DatagramPacket(buf,buf.length,serverSocketAddr);
            socket.send(dpk);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
}
