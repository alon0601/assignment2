package bgu.spl.mics;

import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class MessageBusImplTest {

    private MessageBusImpl messageBus;
    private MicroService micro;
    private MicroService micro2;
    private ExampleEvent e;
    private ExampleBroadcast b;


    @Before
    public void setUp() throws Exception {
        messageBus = MessageBusImpl.getInstance();
        e = new ExampleEvent("do stuff");
        b = new ExampleBroadcast("3");
        micro2 = new GPUService(new GPU(GPU.Type.RTX2080));
        micro = new CPUService(new CPU(3));
        messageBus.register(micro);
        messageBus.register(micro2);
    }

    @After
    public void tearDown() throws Exception {
        messageBus = null;
        micro = null;
        micro2 = null;
    }

    @Test
    public void subscribeEvent() throws InterruptedException {
        messageBus.subscribeEvent(e.getClass(),micro);
        micro2.sendEvent(e);
        assertEquals(e,messageBus.awaitMessage(micro));
    }

    @Test
    public void subscribeBroadcast() throws InterruptedException {
        messageBus.subscribeBroadcast(b.getClass(),micro);
        micro2.sendBroadcast(b);
        assertEquals(b,messageBus.awaitMessage(micro));
    }

    @Test
    public void complete() throws InterruptedException {
        Future<String> f = micro2.sendEvent(e);
        assertFalse(f.isDone());
        messageBus.complete(e,"good!");
        assertTrue(f.isDone());
        assertEquals("good!",f.get());
    }

    @Test
    public void sendBroadcast() throws InterruptedException {
        messageBus.subscribeBroadcast(b.getClass(),micro);
        messageBus.sendBroadcast(b);
        assertEquals(b,messageBus.awaitMessage(micro));
    }

    @Test
    public void sendEvent() throws InterruptedException {
        messageBus.subscribeEvent(e.getClass(),micro);
        messageBus.sendEvent(e);
        assertEquals(e,messageBus.awaitMessage(micro));
    }

    @Test
    public void register() throws InterruptedException {
        assertThrows(IllegalStateException.class,() -> messageBus.awaitMessage(micro2));
        MicroService micro3 = new CPUService(new CPU(4));
        messageBus.register(micro3);
        messageBus.subscribeEvent(e.getClass(),micro3);
        messageBus.sendEvent(e);
        assertEquals(e,messageBus.awaitMessage(micro3));
    }

    @Test
    public void unregister() throws InterruptedException {
        messageBus.unregister(micro2);
        assertThrows(IllegalStateException.class,() -> messageBus.awaitMessage(micro2));
    }

    @Test
    public void awaitMessage() throws InterruptedException {
        Thread mic = new Thread(() -> {
            try {
                messageBus.awaitMessage(micro);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        messageBus.subscribeEvent(e.getClass(),micro);
        messageBus.sendEvent(e);
        assertEquals(e,messageBus.awaitMessage(micro));

        mic.start();
        assertThrows(InterruptedException.class ,() -> messageBus.subscribeEvent(e.getClass(),micro));
    }
}