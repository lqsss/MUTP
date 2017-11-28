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
/*            allPackets.put(count, buf);
            count += bufSize;*/
            byte[] tmpBuf = new byte[readSize];
            System.arraycopy(buf,0,tmpBuf,0,readSize);
            allPackets.put(count, tmpBuf);
            count += readSize;
        }
        return allPackets;
    }
    
    @Test
    public void arrayCopy(){
        int[] arr1 = {1,2,3,4};
        int[] arr2 = new int[4];
        System.arraycopy(arr1,0,arr2,0,4);
        for (int i = 0; i <4 ; i++) {
            System.out.println(arr2[i]);
        }
        arr2[3]=5;
        System.out.println("=========");
        for (int i = 0; i <4 ; i++) {
            System.out.println(arr1[i]);
        }
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


class Son implements ConstTest {
    private int a;
    public int getA() {
        return a;
    }
    public void setA() {
        a = 6;
    }
    public static void main(String[] args) {
        ConstTest son = new Son();
        son.setA();
        System.out.println(son.getA());
    }
}
