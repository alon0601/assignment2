//package bgu.spl.mics.application.objects;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class CPUTest {
//    private static CPU c;
//
//    @Before
//    public void setUp() throws Exception {
//        c = new CPU(3);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void getData() {
//        assertTrue(c.isEmpty());
//        c.getData();
//        assertFalse();
//    }
//
//    @Test
//    public void process() {
//        assertFalse(c.isProcessing());
//        c.process();
//        assertTrue(c.isProcessing());
//    }
//
//    @Test
//    public void sendData() {
//        c.getData();
//        c.process();
//        assertTrue(c.isProcessing());
//        c.sendData(new DataBatch(new Data(Data.Type.Images,1),1));
//        assertTrue(c.isEmpty());
//    }
//
//
//}