package main.common;

/**
 * Created by liqiushi on 2017/11/23.
 */
public class DataPacketFactory {
    public static final String SYN_ONLY = "SYN_ONLY";
    public static final String SYN_ACK = "SYN_ACK";
    public static final String ACK_ONLY = "ACK_ONLY";
    public static final String DATA_ONLY = "DATA_ONLY";
    private DataPacketFactory(){
        
    }
    public static DataPacket getInstance(String pType){
        /**
         * 这里开始连接都ack、seq设为为1
         */
        switch(pType){
            //发出的SYN连接请求
            case SYN_ONLY:
                return new DataPacket(new DataPacketHeader(false,true,0,1));
            //发出的SYN和ACK，确认发送端的连接请求 ACK  ack = 发送端seq+1
            case SYN_ACK:
                return new DataPacket(new DataPacketHeader(true,true,2,1));
            //最终发送ACK  ack = 接收端seq+1 确认连接
            case ACK_ONLY:
                return new DataPacket(new DataPacketHeader(true,false,2,2));
            case DATA_ONLY:
                return new DataPacket(new DataPacketHeader(false,false,-1,-1));
            default:
                break;
        }
        return null;
    }
}
