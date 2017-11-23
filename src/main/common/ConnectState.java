package main.common;

/**
 * Created by liqiushi on 2017/11/23.
 */
public class ConnectState {
    private boolean connected ;
    private boolean synAck ;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isSynAck() {
        return synAck;
    }

    public void setSynAck(boolean synAck) {
        this.synAck = synAck;
    }
}
