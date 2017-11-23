package main.utils;

import main.common.DataPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Created by liqiushi on 2017/11/24.
 */
public class PacketUtil {
    public static void sendPackt(DatagramSocket srcSocket, InetSocketAddress dstSocketAddr, DataPacket dataPacket) throws IOException {
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
}
