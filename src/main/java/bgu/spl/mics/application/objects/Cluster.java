package bgu.spl.mics.application.objects;


import java.util.Collection;
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
	Collection<CPU> CPUS;
	private Map<DataBatch,GPU> unProcessedData;
	private Map<GPU,DataBatch> processedData;


	public static Cluster getInstance() {
		if (cluster == null){
			cluster = new Cluster();
		}
		return cluster;
	}

	public void unprocessedData(DataBatch batch,GPU relevantGpu){
		boolean allProcessing = true;
		unProcessedData.put(batch,relevantGpu);
		while(!unProcessedData.isEmpty())
			for(CPU cpu:CPUS)
				if(!cpu.isProcessing()) {
					allProcessing = false;
					cpu.process(batch);
				}
	}

	public void processedData(DataBatch dataBatch){
		GPU relevantGpu = this.unProcessedData.get(dataBatch);
		relevantGpu.trainModel(dataBatch);
	}

	private Cluster(){
		CPUS = new ConcurrentLinkedDeque<>();
		unProcessedData = new ConcurrentHashMap<>();
		processedData = new ConcurrentHashMap<>();
	}

}
