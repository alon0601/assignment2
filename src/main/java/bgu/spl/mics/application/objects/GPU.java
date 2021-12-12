package bgu.spl.mics.application.objects;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    public GPU() {

    }

    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Model model;
    private boolean haveUnprocesData;

    public GPU(Type type){

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
