package bgu.spl.mics.application.objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


class GPUTest {
    private static GPU g;

    @Before
    public void setUp() throws Exception {
        g = new GPU(GPU.Type.GTX1080);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    void getModel() {
        Model m = new Model();
        g.trainModel(m);
        assertEquals(m,g.getModel());
    }

    @Test
    void trainModel() {
        Model m = new Model();
        g.trainModel(m);
        assertTrue(g.finished());
    }

    @Test
    void sendData() {
        assertTrue(g.HaveUnprocess());
        g.sendData();
        assertFalse(g.HaveUnprocess());
    }
}