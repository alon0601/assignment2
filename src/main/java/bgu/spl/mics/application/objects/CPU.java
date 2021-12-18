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
    private int ticksWork;

    final private Object lock = new Object();

    public CPU(int numOfCors){
        this.cors = numOfCors;
        this.unProcessedData = new ConcurrentLinkedDeque<>();
        this.isProcessing = false;
        cluster = Cluster.getInstance();
        startTick = -1;
        ticksWork = 0;
        this.cluster.addCpu(this);
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public ConcurrentLinkedDeque<DataBatch> getUnProcessedData() {
        return unProcessedData;
    }

    public void getData() {
    }

    public int getTicksWork(){
        return ticksWork;
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public void process() {
        if (!unProcessedData.isEmpty()) {
            DataBatch dataBatch = unProcessedData.getFirst();
            if (startTick == -1){
                startTick = currentTick;
            }
            else {
                if (dataBatch.getData().getType() == Data.Type.Images) {
                    if (this.currentTick - startTick >= (32 / cors) * 4) {
                        ticksWork = ticksWork + (this.currentTick - startTick);
                        sendData(dataBatch);
                        startTick = -1;
                    }
                }
                if (dataBatch.getData().getType() == Data.Type.Text) {
                    if (this.currentTick - startTick >= (32 / cors) * 2) {
                        ticksWork = ticksWork + (this.currentTick - startTick);
                        sendData(dataBatch);
                        startTick = -1;
                    }
                }
                if (dataBatch.getData().getType() == Data.Type.Tabular) {
                    if (this.currentTick - startTick >= (32 / cors)) {
                        ticksWork = ticksWork + (this.currentTick - startTick);
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
