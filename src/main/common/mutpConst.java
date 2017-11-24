package main.common;

import main.utils.PropertiesReader;

/**
 * Created by liqiushi on 2017/11/22.
 */
public class MutpConst {
    public static final String dstPort = PropertiesReader.getInstance().getValue("dstPort");
    public static final String dstHost = PropertiesReader.getInstance().getValue("dstHost");
    public static final String srcPort = PropertiesReader.getInstance().getValue("srcPort");
    public static final String srcHost = PropertiesReader.getInstance().getValue("srcHost");

    public static final String SYN_ONLY = "SYN_ONLY";
    public static final String SYN_ACK = "SYN_ACK";
    public static final String ACK_ONLY = "ACK_ONLY";
    public static final String DATA_ONLY = "DATA_ONLY";
}
