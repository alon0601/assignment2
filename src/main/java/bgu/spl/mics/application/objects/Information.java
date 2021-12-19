package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Student;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Information {
    @Expose private ArrayList<Student> students;
    @Expose private ArrayList<ConfrenceInformation> confrenceInformations;
    @Expose private Cluster cluster = Cluster.getInstance();

    public Information(ArrayList<Student> students,ArrayList<ConfrenceInformation> conference)
    {
        this.students = students;
        this.confrenceInformations = conference;
    }
}
