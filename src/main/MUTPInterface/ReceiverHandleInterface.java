package main.MUTPInterface;

import main.common.DataPacket;
import main.thread.Recevier;

import java.io.IOException;


/**
 * Created by liqiushi on 2017/11/23.
 */
public interface ReceiverHandleInterface {
    //发送方 对应收到的包做处理
    void handle(DataPacket dataPacket,Recevier recevier) throws IOException;
}
