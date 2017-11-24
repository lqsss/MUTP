package test.thread;

import main.utils.PropertiesReader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liqiushi on 2017/11/24.
 */
public class FlieTransmit {
    public Map<Integer, byte[]> readFileInMap(RandomAccessFile accessFile) throws IOException {
        Map<Integer, byte[]> allPackets = new HashMap<>();
        //读取文件流
        int readSize = -1;
        int count = 2;
        int bufSize = Integer.parseInt(PropertiesReader.getInstance().getValue("bufSize"));
        byte[] buf = new byte[bufSize];
        while ((readSize = accessFile.read(buf, 0, buf.length)) != -1) {
            allPackets.put(count, buf);
            count += bufSize;
        }
        return allPackets;
    }

    @Test
    public void test() {
        try {
            String str = PropertiesReader.getInstance().getValue("uploadFile");
            System.out.println(str);
            RandomAccessFile accessFile = new RandomAccessFile(str, "r");
            Map<Integer, byte[]> allPackets = readFileInMap(accessFile);
            System.out.println(allPackets.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
