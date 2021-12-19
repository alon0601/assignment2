package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.ConferenceService;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    @Expose private String name;
    @Expose private int date;
    @Expose private List<Model> models;

    public ConfrenceInformation(String name,int data){
        this.date = data;
        this.name = name;
        models = new ArrayList<>();
    }

    public void addModels(Model model){
        models.add(model);
    }

    @Override
    public String toString(){
        return "name: " + this.name + System.lineSeparator() +
                "date: " + this.date;
    }

    public int getDate() {
        return this.date;
    }

    public List<Model> getModels() {
        return this.models;
    }
}
