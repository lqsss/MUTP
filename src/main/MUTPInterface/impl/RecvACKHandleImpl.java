package main.MUTPInterface.impl;

import main.MUTPInterface.RecvACKHandleInterface;
import main.common.DataPacket;
import main.common.DataPacketHeader;
import main.thread.Recvtor;
import org.apache.log4j.Logger;

/**
 * Created by liqiushi on 2017/11/23.
 */
public class RecvACKHandleImpl implements RecvACKHandleInterface {
    private Logger logger = Logger.getLogger(RecvACKHandleImpl.class);

    @Override
    public void handleAsSYNACK(DataPacket dataPacket, Recvtor recvtor) {
        DataPacketHeader dph = dataPacket.getHeader();
        boolean SYN = dph.isSYN();
        boolean ACK = dph.isACK();
        int seqNum = dph.getSeqNum();
        int ackNum = dph.getAckNum();
        int dupTimes = 1;
        if (SYN && ACK) {
            recvtor.setLastACKed(ackNum);
            logger.info("5800->5900 [SYN ACK] ack = " + ackNum + " seq =" + seqNum);
            recvtor.getConnectState().setSynAck(true);
        } else if (!SYN && ACK&&recvtor.getLastSentedByte() == 0) {
            logger.info("5800->5900 [ACK-C] ack = " + ackNum + " seq =" + seqNum + " Length:" + dataPacket.getBuf().length);
            recvtor.getConnectState().setConnected(true);
            logger.info("connection has established！");
        }else if(!SYN&&ACK && recvtor.getLastSentedByte() != 0){
            logger.info("5800->5900 [ACK] Data = " + ackNum + " seq =" + seqNum + " Length:" + dataPacket.getBuf().length);
            recvtor.setLastACKed(ackNum);
        }
        if (recvtor.getLastACKed() ==ackNum) {
            dupTimes++;
            logger.info("5800->5900 dup ack:"+ackNum);
        }
        
        while(dupTimes >= 3){
            logger.info("transmit again!");
            //重传，并且重设定时器
        }
    }
}
