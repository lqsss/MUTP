package test.thread;

import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liqiushi on 2017/11/29.
 */
public class TimerCheckout {
    private Timer timer;
    public long start;
    public TimerCheckout(){
        timer = new Timer();
        start = System.currentTimeMillis();
    }
    public void goTask(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("===========================");
                System.out.println(System.currentTimeMillis()-start);
            }
        },1000);
    }
    
    public static void main(String[] args) {
        TimerCheckout test = new TimerCheckout();
        test.goTask();
        System.out.println("123");
    }
}
/*public class TimerCheckout {
    private Timer timer;
    public long start;

    public TimerCheckout(){
        this.timer = new Timer();
        start = System.currentTimeMillis();
    }

    public void timerOne(){
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("timerOne invoked ,the time:" + (System.currentTimeMillis() - start));
                try {
                    Thread.sleep(4000);    //线程休眠3000
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    public void timerTwo(){
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("timerOne invoked ,the time:" + (System.currentTimeMillis() - start));
            }
        }, 3000);
    }

    public static void main(String[] args) throws Exception {
        TimerCheckout test = new TimerCheckout();

        test.timerOne();
        test.timerTwo();
    }
}*/

