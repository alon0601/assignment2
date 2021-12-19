package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.StudentService;
import com.google.gson.annotations.Expose;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    public enum Degree {
        MSc, PhD
    }

    @Expose private String name;
    @Expose private String department;
    @Expose private Degree status;
    @Expose private int publications;
    @Expose private int papersRead;
    private List<Model> models;
    @Expose private ConcurrentLinkedDeque<Model> goodModels;
    private ConcurrentLinkedDeque<Model> badModels;

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public Degree getStatus() {
        return status;
    }

    public int getPapersRead() {
        return papersRead;
    }

    public Student(String name, String department, Degree status, List<Model> models){
        this.name = name;
        this.department = department;
        this.status = status ;
        this.publications = 0;
        this.papersRead = 0;
        this.models = models;
        this.goodModels = new ConcurrentLinkedDeque<>();
        this.badModels = new ConcurrentLinkedDeque<>();
    }

    public List<Model> getModels(){
        return this.models;
    }

    public void addGoodModel(Model model){
        this.goodModels.addLast(model);
    }

    public void addBadModel(Model model){
        this.badModels.addLast(model);
    }

    public int getPublications() {
        return publications;
    }

    public void setPublications(int publications) {
        this.publications = publications;
    }

    public void setPapersRead(int papersRead) {
        this.papersRead = papersRead;
    }

    public void addRead(){
        this.papersRead++;
    }

    public void addPublication(){
        this.publications++;
    }
    @Override
    public String toString(){
        return "name : " + this.name + System.lineSeparator() + "department : " + this.department + System.lineSeparator()
                + "Degree : " + this.status + System.lineSeparator()
                + "models : " + this.models + System.lineSeparator();
    }
}
