package main.MUTPInterface.impl;

import main.MUTPInterface.ReceiverHandleInterface;
import main.common.DataPacket;
import main.common.DataPacketFactory;
import main.common.DataPacketHeader;
import main.common.mutpConst;
import main.thread.Recevier;
import main.utils.PacketUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Created by liqiushi on 2017/11/24.
 */
public class RecvHandleImpl implements ReceiverHandleInterface {

    private Logger logger = Logger.getLogger(RecvHandleImpl.class);
    
    @Override
    public void handle(DataPacket dataPacket,Recevier recevier) throws IOException {
        DataPacketHeader dph = dataPacket.getHeader();
        boolean SYN = dph.isSYN();
        boolean ACK = dph.isACK();
        int seqNum = dph.getSeqNum();
        int ackNum = dph.getAckNum();
        DatagramSocket socket = recevier.getSrvSocket();
        InetSocketAddress addr = recevier.getDstSocketAddr();
        
        DataPacket dpk = null;
        //如果是SYN包，返回对等方一个SYN ACK
        if (SYN && !ACK){
            logger.info("5900->5800 [SYN] ack = " + ackNum + " seq =" + seqNum);
            dpk = DataPacketFactory.getInstance(mutpConst.SYN_ACK);
            PacketUtil.sendPackt(socket,addr,dpk);
        }else if(!SYN&&ACK){
            //如果收到的是一个ACK
            logger.info("5900->5800 [ACK] ack = " + ackNum + " seq =" + seqNum);
            dpk = DataPacketFactory.getInstance(mutpConst.SYN_ACK);
            PacketUtil.sendPackt(socket,addr,dpk);
        }else if(!SYN && !ACK){
            //传输文件
            
        }
        
    }
}
