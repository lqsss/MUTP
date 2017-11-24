package main.MUTPInterface;

import main.common.ConnectState;
import main.common.DataPacket;
import main.thread.Recvtor;

/**
 * Created by liqiushi on 2017/11/23.
 */
public interface RecvACKHandleInterface {
    /*void handleAsSYNACK(ConnectState connectState);*/
    //最后一次客户端向服务端发送ACK包，如果服务端接收到 下次也会带上ack
    void handleAsSYNACK(DataPacket dataPacket, Recvtor recvtor);
    int sendSizeLimit = 5;
    int lastACKed = 0;
    int lastSentedByte = 0;

    public int getLastACKed();

    public void setLastACKed(int lastACKed);

    public int getLastSentedByte();

    public void setLastSentedByte(int lastSentedByte);

    public int getSendSizeLimit();

    public void setSendSizeLimit(int sendSizeLimit);

}
