package bgu.spl.mics.application.objects;

import com.google.gson.annotations.Expose;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {
    @Expose private String name;
    @Expose private Data data;
    private Student student;
    /**
     * Enum representing the status of the Model.
     */
    public enum Status {preTrained,Training,Trained,Tested}
    private Status status;
    public enum Results {None,Good,Bad}
    private Results results;

    public Model(String name,Data data){
        this.name = name;
        this.data = data;
        this.status = Status.preTrained;
        this.results = Results.None;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public void setStudent(Student s){
        this.student = s;
    }

    public void setStatus(Status status){
        this.status = status;
    }

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
    @Override
    public String toString(){
        return System.lineSeparator() + "name : " + this.name + System.lineSeparator()
                + "data : " + this.data + System.lineSeparator();
    }
}