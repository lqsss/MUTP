import main.MUTPInterface.ReceiverHandleInterface;
import main.MUTPInterface.impl.RecvHandleImpl;
import main.common.DataPacket;
import main.common.mutpConst;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Created by liqiushi on 2017/11/21.
 */
/*
public class Sender implements Runnable {
    private DatagramSocket srvSocket;
    private ReceiverHandleInterface recvHandle = new RecvHandleImpl();

    private Logger logger = Logger.getLogger(Sender.class);

    public Sender(DatagramSocket srvSocket) {
        this.srvSocket = srvSocket;
    }

    public void handle(DataPacket dataPacket) {
        recvHandle.handle(DataPacket dataPacket);
    }

    @Override
    public void run() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        while (true) {
            byte[] buf = new byte[1024 * 2];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            try {
                srvSocket.receive(dp);
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
}*/
