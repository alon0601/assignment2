package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.Random;

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
                this.gpu.trainModel();
            }
            if (training && this.gpu.finished()){
                System.out.println(Thread.currentThread() + " finished with model: " + this.gpu.getModel().getName() + " at time: " + ticks);
                training = false;
                this.complete(this.currEvent,this.gpu.getModel());
                currEvent = null;
            }
        });

        subscribeEvent(TrainModelEvent.class,callback->{
            if (this.currEvent == null) {
                System.out.println("started training" + Thread.currentThread() + "with model " + callback.getModel().getName() + " at time: " + ticks);
                this.gpu.setModel(callback.getModel());
                this.gpu.sendData();
                this.training = true;
                this.currEvent = callback;
            }
            else{
                this.sendEvent(callback);
            }
        });

        subscribeEvent(TestModelEvent.class,callback->{
            if (this.currEvent == null){
                Random rand = new Random();
                System.out.println(Thread.currentThread() + " is testing - " + callback.getModel().getName() + " at time: " + ticks);
                if (callback.getModel().getStudent().getStatus() == Student.Degree.MSc){
                    if (rand.nextInt(100) < 60){
                        callback.getModel().setResults(Model.Results.Good);
                    }
                    else
                        callback.getModel().setResults(Model.Results.Bad);
                }
                else{
                    if (rand.nextInt(100) < 80){
                        callback.getModel().setResults(Model.Results.Good);
                    }
                    else
                        callback.getModel().setResults(Model.Results.Bad);
                }
                complete(callback,callback.getModel());
                callback.getModel().setStatus(Model.Status.Tested);
            }
            else{
                sendEvent(callback);
            }
        });

        subscribeBroadcast(TerminateAllBroadcast.class,callback->{
            this.gpu.SendGpuTimeToCluster();
            this.terminate();
        });

    }
}
