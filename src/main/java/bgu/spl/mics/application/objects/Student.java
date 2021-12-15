package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.StudentService;

import java.util.List;

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

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private List<Model> models;

    public Student(String name,String department,Degree status,List<Model> models){
        this.name = name;
        this.department = department;
        this.status = status ;
        this.publications = 0;
        this.papersRead = 0;
        this.models = models;
    }

    public List<Model> getModels(){
        return this.models;
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
