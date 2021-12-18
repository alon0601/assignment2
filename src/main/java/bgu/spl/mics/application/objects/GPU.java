package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.GPUService;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {


    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080}

    private Cluster cluster = Cluster.getInstance();
    private Type type;
    private Model model;
    private boolean haveUnProcessData;
    private boolean isFinished = false;
    private int currentTick;
    private List<DataBatch> processedData;
    private int freeCapacity;
    private int indexBatch;
    private int startProTime;
    private int ticksAmount;
    private int ticksWorked;


    public GPU(Type type){
        this.type = type;
        this.indexBatch = 0;
        this.startProTime = -1;
        this.processedData = new ArrayList<>();
        setFreeCapacity();
        ticksWorked = 0;
    }

    public void setFreeCapacity() {
        if (type == Type.GTX1080) {
            freeCapacity = 8;
            ticksAmount = 4;
        }
        else if(type == Type.RTX2080) {
            freeCapacity = 16;
            ticksAmount = 2;
        }
        else {
            freeCapacity = 32;
            ticksAmount = 1;
        }
    }

    public void addProcessed(DataBatch dataBatch) {
        this.processedData.add(dataBatch);
    }

    public boolean finished(){
        return isFinished;
    }

    public void setModel(Model model){
        this.model = model;
        this.model.setStatus(Model.Status.Training);
        this.indexBatch = 0;
        this.setFreeCapacity();
        this.haveUnProcessData = true;
        this.isFinished = false;
    }

    public Model getModel(){
        return this.model;
    }

    public void trainModel() {
        synchronized (this) {
            if (!this.processedData.isEmpty()) {
                if (this.startProTime == -1) {
                    startProTime = this.currentTick;
                }
                else if (this.currentTick > startProTime + ticksAmount) {
                    ticksWorked = ticksAmount + ticksWorked;
                    this.processedData.remove(0);
                    this.model.getData().updateProcess();
                    this.startProTime = -1;
                    this.freeCapacity++;
                    sendData();
                }
            }
            if (this.model.getData().getProcessed() >= this.model.getData().getSize()) {
                this.isFinished = true;
                this.haveUnProcessData = false;
                this.model.setStatus(Model.Status.Trained);
            }
        }
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public void sendData(){
        Data data = model.getData();
        for (int i = 0; i < freeCapacity && indexBatch <= data.getSize(); i++){
            DataBatch batch = new DataBatch(data,this.indexBatch);
            this.indexBatch = this.indexBatch + 1000;
            cluster.unprocessedData(batch,this);
        }
        freeCapacity = 0;
        if (indexBatch >= data.getSize()){
            haveUnProcessData = false;
        }
    }

    public boolean HaveUnProcessData(){
        return haveUnProcessData;
    }

    @Override
    public String toString(){
        return "type : " + this.type + System.lineSeparator();
    }

    public int getTimeWorked(){
        return this.ticksWorked;
    }

    public void SendGpuTimeToCluster(){ //sending the time the gpu worked to the cluster
        cluster.addGpuTime(this.ticksWorked);
    }

}
