package main.client;

import main.common.ConnectState;
import main.common.DataPacket;
import main.common.DataPacketHeader;
import main.common.mutpConst;
import main.thread.Connector;
import main.thread.Recevier;
import main.thread.Recvtor;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by liqiushi on 2017/11/22.
 */
public class Client {
    public static void main(String[] args) {
        
        String dstPort = mutpConst.dstPort;
        String dstHost = mutpConst.dstHost;
        String srcPort = mutpConst.srcPort;
        String srcHost = mutpConst.srcHost;
        InetSocketAddress clientSocketAddr = new InetSocketAddress(srcHost,Integer.parseInt(srcPort));
        InetSocketAddress serverSocketAddr = new InetSocketAddress(dstHost,Integer.parseInt(dstPort));
        ConnectState connectState = new ConnectState();
        try {
            DatagramSocket socket = new DatagramSocket(clientSocketAddr);
            new Thread(new Recvtor(socket, serverSocketAddr, connectState)).start();

            new Thread(new Connector(socket, serverSocketAddr, connectState)).start();
        }catch (SocketException e) {
            e.printStackTrace();
        }

    }
}
