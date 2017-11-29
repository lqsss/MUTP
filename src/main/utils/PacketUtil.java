package main.utils;

import main.common.DataPacket;
import main.common.DataPacketFactory;
import main.common.MutpConst;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liqiushi on 2017/11/24.
 */
public class PacketUtil {
    public static DataPacket pack(byte[] buf){
        DataPacket dpk = DataPacketFactory.getInstance(MutpConst.DATA_ONLY);
        dpk.setBuf(buf);
        return dpk;
    }
    
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

    /**
     * 应用层所有传输的数据
     *
     * @param accessFile
     * @return
     * @throws IOException
     */
    public static Map<Integer, byte[]> readFileInMap(RandomAccessFile accessFile) throws IOException {
        Map<Integer, byte[]> allPackets = new HashMap<>();
        //读取文件流
        int readSize = -1;
        int count = 2;
        int bufSize = Integer.parseInt(PropertiesReader.getInstance().getValue("bufSize"));
        byte[] buf = new byte[bufSize];
        while ((readSize = accessFile.read(buf, 0, buf.length)) != -1) {
            byte[] tmpBuf = new byte[readSize];
            System.arraycopy(buf, 0, tmpBuf, 0, readSize);
            allPackets.put(count, tmpBuf);
            count += readSize;
        }
        return allPackets;
    }
}
