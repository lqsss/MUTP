package main.MUTPInterface.impl;

import main.MUTPInterface.RecvACKHandleInterface;
import main.common.DataPacket;
import main.common.DataPacketHeader;
import main.thread.FileTransmit;
import main.thread.Recvtor;
import org.apache.log4j.Logger;

/**
 * Created by liqiushi on 2017/11/23.
 */
public class RecvACKHandleImpl implements RecvACKHandleInterface {
    private Logger logger = Logger.getLogger(RecvACKHandleImpl.class);
    @Override
    public int getSendSizeLimit() {
        return sendSizeLimit;
    }
    @Override
    public void setSendSizeLimit(int sendSizeLimit) {
        sendSizeLimit = sendSizeLimit;
    }
    @Override
    public int getLastACKed() {
        return lastACKed;
    }
    @Override
    public void setLastACKed(int lastACKed) {
        lastACKed = lastACKed;
    }

    @Override
    public int getLastSentedByte() {
        return lastSentedByte;
    }
    @Override
    public void setLastSentedByte(int lastSentedByte) {
        lastSentedByte = lastSentedByte;
    }
    @Override
    public void handleAsSYNACK(DataPacket dataPacket, Recvtor recvtor) {
        DataPacketHeader dph = dataPacket.getHeader();
        boolean SYN = dph.isSYN();
        boolean ACK = dph.isACK();
        int seqNum = dph.getSeqNum();
        int ackNum = dph.getAckNum();
        int dupTimes = 1;
        if (SYN && ACK) {
            logger.info("5800->5900 [SYN ACK] ack = " + ackNum + " seq =" + seqNum);
            recvtor.getConnectState().setSynAck(true);
        } else if (!SYN && ACK && getLastSentedByte() == 0) {
            logger.info("5800->5900 [ACK-C] ack = " + ackNum + " seq =" + seqNum + " Length:" + dataPacket.getBuf().length);
            recvtor.getConnectState().setConnected(true);
            //连接成功
            logger.info("connection has established！");
            setLastSentedByte(-1);//可以发送了
        } else if (!SYN && ACK && getLastSentedByte() != 0) {
            logger.info("5800->5900 [ACK] Data = " + ackNum + " seq =" + seqNum + " Length:" + dataPacket.getBuf().length + " windowSize:" + dataPacket.getWindowSize());
            if (getLastACKed() == ackNum) {
                dupTimes++;
                logger.info("5800->5900 dup ack:" + ackNum);
            }
            while (dupTimes >= 3) {
                logger.info("transmit again!");
                //重传，并且重设定时器   
                dupTimes= 1;
            }
            //启动文件发送线程
            setSendSizeLimit(dataPacket.getWindowSize());
            new Thread(new FileTransmit(this)).start();
            setLastACKed(ackNum);
        }
    }
}
