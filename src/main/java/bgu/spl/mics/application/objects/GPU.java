package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.GPUService;
import sun.util.calendar.Gregorian;

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

    private Type type;
    private Model model;
    private boolean haveUnProcessData;
    private boolean isFinished = false;
    private int currentTick;
    private List<DataBatch> processedData;


    public GPU(Type type){
        this.type = type;
        this.processedData = new ArrayList<>();
    }

    public boolean finished(){
        return isFinished;
    }

    public Model getModel(){
        return this.model;
    }

    public void trainModel(DataBatch data){
        int start = currentTick;
        this.processedData.add(data);
        if(processedData.size() >= model.getData().getSize()/1000){
            isFinished = true;
            this.model.setStatus(Model.Status.Trained);
        }
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public void sendData(Model model){
        haveUnProcessData = true;
        Data data = model.getData();
        for (int i = 0; i < data.getSize();i = i + 1000){
            Cluster.getInstance().unprocessedData(new DataBatch(data,i),this);
        }
        haveUnProcessData = false;
    }

    public boolean HaveUnProcessData(){
        return haveUnProcessData;
    }

}
