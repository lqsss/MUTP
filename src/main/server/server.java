package main.server;

import main.common.DataPacket;
import main.common.mutpConst;
import main.thread.Recevier;

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
        try {
            DatagramSocket socket = new DatagramSocket(serverSocketAddr);
            new Thread(new Recevier(socket,clientSocketAddr)).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
      
    }
}
