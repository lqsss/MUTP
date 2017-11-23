package main.common;

import main.utils.PropertiesReader;

import java.beans.Transient;
import java.io.Serializable;

/**
 * Created by liqiushi on 2017/11/22.
 */
public class DataPacket implements Serializable {
    private DataPacketHeader header;
    private byte[] buf ;
   
    private String bufSize = PropertiesReader.getInstance().getValue("bufSize");

    public DataPacket(DataPacketHeader header, byte[] buf) {
        this.header = header;
        this.buf = buf;
    }

    public DataPacket(DataPacketHeader header) {
        this.header = header;
        this.buf = new byte[Integer.parseInt(bufSize.trim())];
        header.setPacketLength(Integer.parseInt(bufSize.trim()));
    }
    
    public DataPacketHeader getHeader() {
        return header;
    }

    public void setHeader(DataPacketHeader header) {
        this.header = header;
    }

    //@Transient
    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }
    
}
