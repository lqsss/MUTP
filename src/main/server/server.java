package main.server;

import main.common.DataPacket;
import main.common.mutpConst;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by liqiushi on 2017/11/22.
 */
public class server {
    public static void main(String[] args) {
        String dstPort = mutpConst.dstPort;
        String dstHost = mutpConst.dstHost;
        String srcPort = mutpConst.srcPort;
        String srcHost = mutpConst.srcHost;
        InetSocketAddress clientSocketAddr = new InetSocketAddress(srcHost, Integer.parseInt(srcPort));
        InetSocketAddress serverSocketAddr = new InetSocketAddress(dstHost, Integer.parseInt(dstPort));
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(serverSocketAddr);
//            DataPacketHeader dph = new DataPacketHeader(true,true,12,1);
//            DataPacket dp = new DataPacket(dph);
            byte recvBuf[] = new byte[1024 * 100];

            DatagramPacket recvDp = new DatagramPacket(recvBuf, recvBuf.length);
            while (true) {
                System.out.println("发送前包的大小:" + recvDp.getLength());
                socket.receive(recvDp);
                System.out.println("发送后包的大小:" + recvDp.getLength());

                bais = new ByteArrayInputStream(recvBuf);
                ois = new ObjectInputStream(bais);
                DataPacket dataPacket = (DataPacket) ois.readObject();
                System.out.println(dataPacket);
            }
/*            byte buf[] = baos.toByteArray();
            DatagramPacket dpk = new DatagramPacket(buf,buf.length,serverSocketAddr);
            socket.send(dpk);*/
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }
}
