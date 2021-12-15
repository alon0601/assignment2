package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.TimeService;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    private int cors;
    private Collection<DataBatch> unProcessedData;
    private Collection<DataBatch> processedData;
    private boolean isProcessing = false;
    private Cluster cluster;
    private int currentTick;//not sure if we can do this

    final private Object lock = new Object();

    public CPU(int numOfCors){
        this.cors = numOfCors;
        this.unProcessedData = new ConcurrentLinkedDeque<>();
        this.processedData = new ConcurrentLinkedDeque<>();
        cluster = Cluster.getInstance();
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public boolean isEmpty() {
        return true;
    }

    public void getData() {
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public void process(DataBatch dataBatch) {
        synchronized (lock) {
            int start = currentTick;
            if (dataBatch.getData().getType() == Data.Type.Images) {
                if (this.currentTick - start >= (32 / cors) * 4)
                    sendData(dataBatch);
            }
            if (dataBatch.getData().getType() == Data.Type.Images) {
                if (this.currentTick - start >= (32 / cors) * 2)
                    sendData(dataBatch);
            }
            if (dataBatch.getData().getType() == Data.Type.Images) {
                if (this.currentTick - start >= (32 / cors))
                    sendData(dataBatch);
            }
        }

    }

    public void sendData(DataBatch dataBatch) {
        cluster.processedData(dataBatch);
    }
}
