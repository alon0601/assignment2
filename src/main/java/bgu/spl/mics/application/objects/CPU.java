package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.TimeService;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    private int cors;
    private ConcurrentLinkedDeque<DataBatch> unProcessedData;
    private boolean isProcessing = false;
    private Cluster cluster;
    private int currentTick;//not sure if we can do this
    private int startTick;

    final private Object lock = new Object();

    public CPU(int numOfCors){
        this.cors = numOfCors;
        this.unProcessedData = new ConcurrentLinkedDeque<>();
        cluster = Cluster.getInstance();
        startTick = -1;
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

    public void process() {
        DataBatch dataBatch = unProcessedData.getFirst();
        if (!unProcessedData.isEmpty()) {
            if (startTick == -1){
                startTick = currentTick;
            }
            else {
                if (dataBatch.getData().getType() == Data.Type.Images) {
                    if (this.currentTick - startTick >= (32 / cors) * 4) {
                        sendData(dataBatch);
                        startTick = -1;
                    }
                }
                if (dataBatch.getData().getType() == Data.Type.Images) {
                    if (this.currentTick - startTick >= (32 / cors) * 2) {
                        sendData(dataBatch);
                        startTick = -1;
                    }
                }
                if (dataBatch.getData().getType() == Data.Type.Images) {
                    if (this.currentTick - startTick >= (32 / cors)) {
                        sendData(dataBatch);
                        startTick = -1;
                    }
                }
            }
        }
    }

    public void sendData(DataBatch dataBatch) {
        cluster.processedData(dataBatch);
        this.unProcessedData.remove(dataBatch);
    }

    public void addBatch(DataBatch batch) {
        unProcessedData.add(batch);
    }

    public boolean haveUnProcessedData(){
        return unProcessedData.isEmpty();
    }
}
