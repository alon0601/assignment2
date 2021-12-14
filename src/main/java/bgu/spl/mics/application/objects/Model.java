package bgu.spl.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {
    private String name;
    private Data data;
    private Student student;
    /**
     * Enum representing the status of the Model.
     */
    enum Status {preTrained,Training,Trained,Tested}
    private Status status;
    enum Results {None,Good,Bad}
    private Results results;

    public Data getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public Results getResults() {
        return results;
    }

    public Status getStatus() {
        return status;
    }

    public Student getStudent() {
        return student;
    }
}