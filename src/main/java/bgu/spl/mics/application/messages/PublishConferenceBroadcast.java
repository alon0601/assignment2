package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.LinkedList;
import java.util.List;

public class PublishConferenceBroadcast implements Broadcast {
    private List<Model> models;

    public PublishConferenceBroadcast(){
        models = new LinkedList<>();
    }

    public PublishConferenceBroadcast(List<Model> models) {
        this.models = models;
    }

    public void addModel(Model model){
        this.models.add(model);
    }

    public List<Model> getModels(){
        return models;
    }
}
