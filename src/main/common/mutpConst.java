package main.common;

import main.utils.PropertiesReader;

/**
 * Created by liqiushi on 2017/11/22.
 */
public class mutpConst {
    public static final String dstPort = PropertiesReader.getInstance().getValue("dstPort");
    public static final String dstHost = PropertiesReader.getInstance().getValue("dstHost");
    public static final String srcPort = PropertiesReader.getInstance().getValue("srcPort");
    public static final String srcHost = PropertiesReader.getInstance().getValue("srcHost");
}
