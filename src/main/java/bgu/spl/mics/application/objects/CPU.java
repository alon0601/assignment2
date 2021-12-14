package bgu.spl.mics.application.objects;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    private int cors;
    private Collection<DataBatch> processing;
    private Cluster cluster;

    public CPU(int numOfCors){
        this.cors = numOfCors;
        this.processing = new ConcurrentLinkedDeque<>();
        cluster = Cluster.getInstance();
    }

    public boolean isEmpty() {
        return true;
    }

    public void getData() {
    }

    public boolean isProcessing() {
        return true;
    }

    public void process() {
    }

    public void sendData() {
    }
}
