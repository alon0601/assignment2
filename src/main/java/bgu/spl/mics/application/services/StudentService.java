package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;


import java.util.List;

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

    public StudentService(Student student) {
        super("studentService");
        this.student = student;
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
        System.out.println(student.getModels().get(0));
        for (Model model:student.getModels()){
            Future<Model> trainModel= sendEvent(new TrainModelEvent(model));
            model.setStatus(Model.Status.preTrained);
            Future<Model> testModel = sendEvent(new TestModelEvent(trainModel.get()));
            if (testModel.get().getResults() == Model.Results.Good){
                sendEvent(new PublishResultsEvent(testModel.get()));
            }
        }
    }
}
