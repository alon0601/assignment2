package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private GPU gpu;
    private int ticks;


    public GPUService(GPU gpu) {
        super("GPU");
        this.gpu = gpu;
        ticks = 1;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class,callback-> {
            this.ticks++;
            gpu.setCurrentTick(ticks);
        });
        subscribeEvent(TrainModelEvent.class,callback->{
            this.gpu.sendData(callback.getModel());
            while(!this.gpu.finished());
            this.messageBus.complete(callback, gpu.getModel());
            subscribeEvent(TestModelEvent.class,test->{
                System.out.printf("");
            });

        });

    }
}
