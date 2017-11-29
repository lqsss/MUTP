package main.MUTPInterface.impl;

import main.MUTPInterface.ReceiverHandleInterface;
import main.common.DataPacket;
import main.common.DataPacketFactory;
import main.common.DataPacketHeader;
import main.common.MutpConst;
import main.thread.Recevier;
import main.utils.PacketUtil;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liqiushi on 2017/11/24.
 */
public class RecvHandleImpl implements ReceiverHandleInterface {

    private Logger logger = Logger.getLogger(RecvHandleImpl.class);

    @Override
    public void handle(DataPacket dataPacket, Recevier recevier) throws IOException {
        DataPacketHeader dph = dataPacket.getHeader();
        boolean SYN = dph.isSYN();
        boolean ACK = dph.isACK();
        int seqNum = dph.getSeqNum();
        int ackNum = dph.getAckNum();
        DatagramSocket socket = recevier.getSrvSocket();
        InetSocketAddress addr = recevier.getDstSocketAddr();
        Map<Integer, byte[]> tmpList = recevier.getTmpList();
        int expectSeqnum = recevier.getExpectSeqnum();
        //<ackNum，byte[]>

        DataPacket dpk = null;
        //如果是SYN包，返回对等方一个SYN ACK
        if (SYN && !ACK) {
            logger.info("5900->5800 [SYN] ack = " + ackNum + " seq =" + seqNum);
            dpk = DataPacketFactory.getInstance(MutpConst.SYN_ACK);
            PacketUtil.sendPackt(socket, addr, dpk);
        } else if (!SYN && ACK) {
            //如果收到的是一个ACK
            logger.info("5900->5800 [ACK] ack = " + ackNum + " seq =" + seqNum);
            dpk = DataPacketFactory.getInstance(MutpConst.ACK_ONLY);
            dpk.getHeader().setAckNum(seqNum + 1);
            dpk.setWindowSize(5);
            PacketUtil.sendPackt(socket, addr, dpk);
        } else if (!SYN && !ACK) {
            logger.info("5900 -> 5800 Data seq = "+seqNum);
            DataPacket ackPacket = DataPacketFactory.getInstance(MutpConst.ACK_ONLY);
            //如果是跟前面的连起来 就写入文件 并且返回一个当前最大的ack
            if (tmpList.size() < 5) {
                tmpList.put(seqNum, dataPacket.getBuf());

                if (dataPacket.getHeader().getSeqNum() != expectSeqnum) {
                    //如果不相等 则返回map中key最小的ackNum
                    ackPacket.getHeader().setAckNum(expectSeqnum);

                } else {
                    Iterator<Map.Entry<Integer, byte[]>> it = tmpList.entrySet().iterator();

                    fileTransmit(it, expectSeqnum, recevier);
                    ackPacket.getHeader().setAckNum(recevier.getExpectSeqnum());
                }
                
                if(tmpList.size() == 5){
                    ackPacket.setWindowSize(1);
                }else{
                    ackPacket.setWindowSize(5 - tmpList.size());
                }
  
            }else if(tmpList.size() == 5){
                 ackPacket = DataPacketFactory.getInstance(MutpConst.ACK_ONLY);
                //不是需要的就丢弃
                if (dataPacket.getHeader().getSeqNum() != expectSeqnum) {
                    //如果不相等 则返回map中key最小的ackNum
                    ackPacket.getHeader().setAckNum(expectSeqnum);
                    logger.info("丢弃: seq = "+dataPacket.getHeader().getSeqNum());
                    ackPacket.setWindowSize(1);
                }else{
                    tmpList.put(seqNum, dataPacket.getBuf());
                    Iterator<Map.Entry<Integer, byte[]>> it = tmpList.entrySet().iterator();
                    fileTransmit(it, expectSeqnum, recevier);
                    ackPacket.setWindowSize(5 - tmpList.size());
                    ackPacket.getHeader().setAckNum(recevier.getExpectSeqnum());
                }
            }
           
            logger.info("ack = " +ackPacket.getHeader().getAckNum());
            logger.info("还可以发送 windowSize =  " +ackPacket.getWindowSize());
            try {
                PacketUtil.sendPackt(socket, addr, ackPacket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
    }

    public void fileTransmit(Iterator<Map.Entry<Integer, byte[]>> iterator, int seqNum, Recevier recevier) throws IOException {
        if (iterator.hasNext()) {
            //Iterator<Map.Entry<Integer, byte[]>> tmpIt = iterator;
            Map.Entry entry = iterator.next();
            if ((int) entry.getKey() == seqNum) {
                //写文件
                BufferedOutputStream bos = recevier.getBos();
                byte[] fileByte = (byte[]) entry.getValue();
                bos.write(fileByte, 0, fileByte.length);
                recevier.setExpectSeqnum((int) entry.getKey() + fileByte.length);
                iterator.remove();
                fileTransmit(iterator, (int) entry.getKey() + fileByte.length, recevier);
            } else {
                recevier.setExpectSeqnum(seqNum);
                return;
            }
        }
    }

}


