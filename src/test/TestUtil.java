package test;

import main.utils.PropertiesReader;
import org.junit.Test;

/**
 * Created by liqiushi on 2017/11/22.
 */
public class TestUtil {
    @Test
    public void testPropertiesUtil(){
        String string = PropertiesReader.getInstance().getValue("bufSize");
        System.out.println(Integer.parseInt(string.trim()));
    }
}
