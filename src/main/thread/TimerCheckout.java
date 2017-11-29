package main.thread;

import main.MUTPInterface.RecvACKHandleInterface;
import main.common.DataPacket;
import main.utils.PacketUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时任务
 * Created by liqiushi on 2017/11/29.
 */

public class TimerCheckout {
    private Timer timer;
    public long start;
    private RecvACKHandleInterface impl;
    private DataPacket dpk;
    private DatagramSocket cliSocket;
    private InetSocketAddress dstSocketAddr;
    private Logger logger = Logger.getLogger(TimerCheckout.class);

    public TimerCheckout(RecvACKHandleInterface impl, DataPacket dpk, DatagramSocket cliSocket, InetSocketAddress dstSocketAddr) {
        timer = new Timer();
        start = System.currentTimeMillis();
        this.impl = impl;
        this.dpk = dpk;
        this.cliSocket = cliSocket;
        this.dstSocketAddr = dstSocketAddr;
    }

    /**
     * 给每一个发送的包设一个超时重传的任务
     * 在3s内若自身未收到对等方的ack响应包，则重传
     */
    public void goTask() {
        timer.schedule(new TimerTask() {
            //如果lastAcked小于当前包seq+length，重发
            @Override
            public void run() {
                logger.info("=====================");
                if(impl.getLastACKed() < dpk.getHeader().getSeqNum() + dpk.getBuf().length) {
                    try {
                        logger.info("收到ack = " + impl.getLastACKed() + "重传 seq = " + dpk.getHeader().getSeqNum());
                        PacketUtil.sendPackt(cliSocket, dstSocketAddr, dpk);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 3000);
    }

/*    public static void main(String[] args) {
        TimerCheckout test = new TimerCheckout();
        test.goTask();
    }*/
}
