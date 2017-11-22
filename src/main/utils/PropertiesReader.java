package main.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by liqiushi on 2017/11/22.
 */
public class PropertiesReader {


    private Properties pros = null;

    private static class ConfigurationHolder {
        private static PropertiesReader configuration = new PropertiesReader();
    }

    public static PropertiesReader getInstance() {
        return ConfigurationHolder.configuration;
    }

    public String getValue(String key) {
        return pros.getProperty(key);
    }

    private PropertiesReader() {
        readConfig();
    }


    private void readConfig() {
        pros = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(Thread.currentThread().getContextClassLoader().getResource("")
                    .getPath() + "args.properties");
            pros.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
