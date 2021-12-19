package bgu.spl.mics.application.objects;


import com.google.gson.annotations.Expose;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {
	/**
     * Retrieves the single instance of this class.
     */
	private static Cluster cluster = null;
	private ConcurrentLinkedDeque<CPU> CPUS;
	private Map<DataBatch,GPU> unProcessedData;
	@Expose private AtomicInteger cpusTime;
	@Expose private AtomicInteger gpusTime;
	@Expose private AtomicInteger numOfBach;


	public static Cluster getInstance() {
		if (cluster == null){
			cluster = new Cluster();
		}
		return cluster;
	}

	public void unprocessedData(DataBatch batch,GPU relevantGpu){
		synchronized (CPUS) {
			unProcessedData.put(batch, relevantGpu);
			CPU cpu = CPUS.removeFirst();
			cpu.addBatch(batch);
			CPUS.addLast(cpu);
		}
	}

	public void processedData(DataBatch dataBatch){
		GPU relevantGpu = this.unProcessedData.get(dataBatch);
		synchronized (relevantGpu) {
			numOfBach.getAndIncrement();
			relevantGpu.addProcessed(dataBatch);
		}
	}

	public void addCpu(CPU cpu){
		this.CPUS.add(cpu);
	}

	public int getCpusTime(){
		for (CPU c: cluster.CPUS){
			cpusTime.addAndGet(c.getTicksWork());
		}
		return (cpusTime.intValue());
	}

	public int getGpusTime(){
		return gpusTime.intValue();
	}

	public int getNumOfBach(){
		return numOfBach.intValue();
	}

	public void addGpuTime(int gpuTime){
		this.gpusTime.addAndGet(gpuTime);
	}

	private Cluster(){
		CPUS = new ConcurrentLinkedDeque<CPU>();
		unProcessedData = new ConcurrentHashMap<>();
		cpusTime = new AtomicInteger(0);
		gpusTime = new AtomicInteger(0);
		numOfBach = new AtomicInteger(0);
	}

}
