package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;

/**
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {

    private CPU cpu;
    private int ticks;

    public CPUService(String name,CPU cpu) {
        super(name);
        this.cpu = cpu;
        ticks = 1;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, callback->{
            ticks++;
            cpu.setCurrentTick(ticks);
        });
    }
}
