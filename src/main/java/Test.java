import bgu.spl.mics.Future;

import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {
        Future<String> f= new Future<>();
        Thread t = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(4);
                f.resolve("good");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Future f1 = new Future();
        Thread t2 = new Thread(()->{
            System.out.println(f.get(30,TimeUnit.SECONDS));
        });
        t.start();
        t2.start();
        System.out.println(f.get(10, TimeUnit.SECONDS));
    }
}
