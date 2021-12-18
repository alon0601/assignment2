package bgu.spl.mics.application.objects;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

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


	public static Cluster getInstance() {
		if (cluster == null){
			cluster = new Cluster();
		}
		return cluster;
	}

	public void unprocessedData(DataBatch batch,GPU relevantGpu){
		unProcessedData.put(batch, relevantGpu);
		CPU cpu = CPUS.removeFirst();
		cpu.addBatch(batch);
		CPUS.addLast(cpu);
	}

	public void processedData(DataBatch dataBatch){
		GPU relevantGpu = this.unProcessedData.get(dataBatch);
		synchronized (relevantGpu) {
			relevantGpu.addProcessed(dataBatch);
		}
	}

	public void addCpu(CPU cpu){
		this.CPUS.add(cpu);
	}

	private Cluster(){
		CPUS = new ConcurrentLinkedDeque<CPU>();
		unProcessedData = new ConcurrentHashMap<>();
	}

}
