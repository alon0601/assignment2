package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.GPU;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private GPU gpu;
    private int ticks;


    public GPUService(String name,GPU gpu) {
        super(name);
        this.gpu = gpu;
        ticks = 1;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class,callback-> {
            this.ticks++;
        });
        subscribeEvent(TrainModelEvent.class,callback->{
            this.gpu.sendData(callback.getModel());

        });

    }
}
