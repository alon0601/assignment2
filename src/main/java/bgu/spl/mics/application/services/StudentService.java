package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import com.google.gson.annotations.Expose;


import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {

    private Student student;
    private boolean working;
    private int ticks;
    private Future<Model> currModel;
    private boolean sendTest;

    public StudentService(Student student) {
        super("studentService");
        this.student = student;
        this.working = false;
        this.ticks = 0;
        currModel = null;
        sendTest = false;
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(PublishConferenceBroadcast.class, callback->{
            List<Model> listModels = callback.getModels();
            for (Model m:listModels){
                if (m.getStudent() == this.student){
                    this.student.addPublication();
                }
                else{
                    this.student.addRead();
                }
            }
        });
        this.subscribeBroadcast(TickBroadcast.class,callback->{
            ticks++;
            if (!this.student.getModels().isEmpty()) {
                if (!this.working) {
                    this.working = true;
                    Model model = student.getModels().get(0);
                    model.setStudent(this.student);
                    model.setStatus(Model.Status.Training);
                    while (currModel == null) {
                        currModel = sendEvent(new TrainModelEvent(model));
                    }
                } else {
                    Model model = currModel.get(1, TimeUnit.MILLISECONDS);
                    if (model != null) { //make sure its change
                        if (!sendTest) {
                            sendEvent(new TestModelEvent(model));
                            sendTest = true;
                        }
                        if (model.getResults() == Model.Results.Good) {
                            sendEvent(new PublishResultsEvent(model));
                            this.student.addGoodModel(model);
                            this.student.getModels().remove(model);
                            this.working = false;
                            this.currModel = null;
                            this.sendTest = false;
                        }
                        if (model.getResults() == Model.Results.Bad) {
                            this.student.addBadModel(model);
                            this.student.getModels().remove(model);
                            this.working = false;
                            this.currModel = null;
                            this.sendTest = false;
                        }
                    }
                }
            }
        });
        subscribeBroadcast(TerminateAllBroadcast.class,callback->{
            this.terminate();
        });
    }
}
