package main.MUTPInterface.impl;

import main.MUTPInterface.RecvACKHandleInterface;
import main.common.DataPacket;
import main.common.DataPacketFactory;
import main.common.DataPacketHeader;
import main.common.MutpConst;
import main.thread.FileTransmit;
import main.thread.Recvtor;
import main.thread.TimerCheckout;
import main.utils.PacketUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * Created by liqiushi on 2017/11/23.
 */
public class RecvACKHandleImpl implements RecvACKHandleInterface {
    private Logger logger = Logger.getLogger(RecvACKHandleImpl.class);
    private int sendSizeLimit;
    private int lastACKed;
    private int lastSentedByte;

    @Override
    public int getSendSizeLimit() {
        return sendSizeLimit;
    }

    @Override
    public void setSendSizeLimit(int sendSizeLimit) {
        this.sendSizeLimit = sendSizeLimit;
    }

    @Override
    public synchronized int getLastACKed() {
        return lastACKed;
    }

    @Override
    public synchronized void setLastACKed(int lastACKed) {
        this.lastACKed = lastACKed;
    }

    @Override
    public int getLastSentedByte() {
        return lastSentedByte;
    }

    @Override
    public void setLastSentedByte(int lastSentedByte) {
        this.lastSentedByte = lastSentedByte;
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
            logger.info("5800->5900 [ACK-C] ack = " + ackNum + " seq =" + seqNum + " Length:" + dataPacket.getBuf().length + " windowSize:" + dataPacket.getWindowSize());
            recvtor.getConnectState().setConnected(true);
            //连接成功
            logger.info("connection has established！");
            setLastSentedByte(-1);//可以发送了
            setSendSizeLimit(dataPacket.getWindowSize());
            new FileTransmit(this, recvtor.getCliSocket(), recvtor.getDstSocketAddr()).run();
        } else if (!SYN && ACK && getLastSentedByte() != 0) {
            logger.info("5800->5900 [ACK] Data ackNum= " + ackNum + " seq =" + seqNum + " Length:" + dataPacket.getBuf().length + " windowSize:" + dataPacket.getWindowSize());
            if (getLastACKed() == ackNum) {
                dupTimes++;
                logger.info("5800->5900 dup ack:" + ackNum);
            }
            if (dupTimes >= 3) {
                logger.info("transmit again!");
                //Todo 定时任务
                //重传，并且重设定时器  
                
                    Map<Integer, byte[]> allPackets = recvtor.getAllPackets();
                    DataPacket reSendDpk = DataPacketFactory.getInstance(MutpConst.DATA_ONLY);
                    reSendDpk.setBuf(allPackets.get(ackNum));
                    reSendDpk.getHeader().setSeqNum(ackNum);
                    try {
                        PacketUtil.sendPackt(recvtor.getCliSocket(), recvtor.getDstSocketAddr(), reSendDpk);
                        //new TimerCheckout(this,reSendDpk,recvtor.getCliSocket(),recvtor.getDstSocketAddr()).goTask();
                        logger.info("重传 " + reSendDpk.getHeader().getSeqNum());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                setLastACKed(ackNum);
                //启动文件发送线程(XXXXX)
                setSendSizeLimit(dataPacket.getWindowSize());
                logger.info(getSendSizeLimit());
                if(getLastSentedByte() - getLastACKed() <= 5){
                    new FileTransmit(this, recvtor.getCliSocket(), recvtor.getDstSocketAddr()).run();
                }
                

                //
            }
        }
    
/*    private void fileSendFun(int ackNum,int dupTimes,DataPacket dataPacket,int seqNum,Recvtor recvtor){
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
        new Thread(new FileTransmit(this,recvtor.getCliSocket(),recvtor.getDstSocketAddr())).start();
        setLastACKed(ackNum);
    }*/
    }
