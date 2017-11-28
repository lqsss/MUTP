package test.thread;

import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by liqiushi on 2017/11/28.
 */
public class Receiver {
    @Test
    public void testTreeMap() {
        Map<Integer, String> map = new TreeMap<>();
        map.put(2, "two");
        map.put(4, "four");
        map.put(1, "one");
        map.put(3, "three");

        Iterator<Map.Entry<Integer, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            System.out.println(entry.getValue());
        }
    }

    @Test
    public void sortTest() {
        TestSort t1 = new TestSort();
        TestSort t2 = new TestSort();
        t1.num = 3;
        t2.num = 2;

        Map<TestSort, String> map = new TreeMap<>();
        map.put(t1, "three");
        map.put(t2, "two");
        Iterator<Map.Entry<TestSort, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            System.out.println(entry.getValue());
        }
    }
}


class TestSort implements Comparable<TestSort> {
    public Integer num;

    @Override
    public int compareTo(TestSort o) {
        if (this.num > o.num)
            return 1;
        else if (this.num < o.num)
            return -1;
        else return 0;
    }
}
