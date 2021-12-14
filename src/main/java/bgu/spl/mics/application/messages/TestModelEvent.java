package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;
import com.sun.org.apache.xpath.internal.operations.Mod;

public class TestModelEvent implements Event {

    private Model model;
    public TestModelEvent(Model model) {
        this.model = model;
    }
}
