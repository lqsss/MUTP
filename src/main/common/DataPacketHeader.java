package main.common;

import java.io.Serializable;

public class DataPacketHeader implements Serializable {
    private boolean ACK;
    private boolean SYN;
    private int ackNum;
    private int seqNum;
    private int packetLength;

    public DataPacketHeader(boolean ACK, boolean SYN, int ackNum, int seqNum) {
        this.ACK = ACK;
        this.SYN = SYN;
        this.ackNum = ackNum;
        this.seqNum = seqNum;
        //this.packetLength = packetLength;
    }

    public boolean isACK() {
        return ACK;
    }

    public void setACK(boolean ACK) {
        this.ACK = ACK;
    }

    public boolean isSYN() {
        return SYN;
    }

    public void setSYN(boolean SYN) {
        this.SYN = SYN;
    }

    public int getAckNum() {
        return ackNum;
    }

    public void setAckNum(int ackNum) {
        this.ackNum = ackNum;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public int getPacketLength() {
        return packetLength;
    }

    public void setPacketLength(int packetLength) {
        this.packetLength = packetLength;
    }
}
