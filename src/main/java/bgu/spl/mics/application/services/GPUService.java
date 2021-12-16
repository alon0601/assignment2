package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
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
    private boolean training;
    private Event currEvent;


    public GPUService(GPU gpu) {
        super("GPU");
        this.gpu = gpu;
        ticks = 0;
        training = false;
        currEvent = null;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class,callback-> {
            this.ticks++;
            this.gpu.setCurrentTick(ticks);
            if (training) {
                this.gpu.sendData();
                this.gpu.trainModel();
            }
            if (training && this.gpu.finished()){
                System.out.println(Thread.currentThread());
                training = false;
                this.complete(this.currEvent,this.gpu.getModel());
                currEvent = null;
            }
        });
        subscribeEvent(TrainModelEvent.class,callback->{
            System.out.println("started training" + Thread.currentThread() + "with model " + callback.getModel().getName());
            this.gpu.setModel(callback.getModel());
            this.gpu.sendData();
            this.training = true;
            this.currEvent = callback;
        });

    }
}
