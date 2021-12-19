package bgu.spl.mics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class FutureTest {

    private static Future<String> future;

    @Before
    public void setUp() throws Exception{
        future = new Future<String>();
    }

    @After
    public void tearDown() throws Exception{
        future = null;
    }

    @Test
    public void getTest() {
        Thread micro = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.resolve("test12");
        });
        micro.start();
        String ans = future.get();
        assertEquals("test12",ans);
    }

    @Test
    public void resolveTest() {
        future.resolve("test1");
        assertEquals("test1",future.get());
    }

    @Test
    public void isDoneTest(){
        assertFalse(future.isDone());
        future.resolve("hello");
        assertTrue(future.isDone());
    }

    @Test
    public void getWithTimeTest(){
        Thread micro = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.resolve("test");
        });
        Instant start = Instant.now();
        micro.start();
        String ans = future.get(5,TimeUnit.SECONDS);
        Instant stop = Instant.now();
        assertEquals("test",ans);
        assertTrue(stop.getEpochSecond() - start.getEpochSecond() <= 5);


        Thread micro2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.resolve("test2");
        });
        future.resolve(null);
        start = Instant.now();
        micro2.start();
        ans = future.get(2,TimeUnit.SECONDS);
        stop = Instant.now();
        assertNull(ans);
        assertEquals(2, stop.getEpochSecond() - start.getEpochSecond());
    }
}
