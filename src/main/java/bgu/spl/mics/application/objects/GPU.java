package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.GPUService;
import sun.util.calendar.Gregorian;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Model model;
    private boolean haveUnprocesData;
    private GPUService Gpu;

    public GPU(Type type){
        this.type = type;
    }

    public boolean finished(){
        return true;
    }
    public Model getModel(){
        return this.model;
    }

    public void trainModel(Model model){

    }

    public void sendData(){

    }

    public boolean HaveUnprocess(){
        return haveUnprocesData;
    }

}
