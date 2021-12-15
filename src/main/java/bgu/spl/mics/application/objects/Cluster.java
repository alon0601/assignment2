package bgu.spl.mics.application.objects;


import java.util.Collection;
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
	Collection<GPU> GPUS ;
	Collection<CPU> CPUS;



	public static Cluster getInstance() {
		if (cluster == null){
			cluster = new Cluster();
		}
		return cluster;
	}

	public void unprocessedData(DataBatch batch,GPU relevantGpu){

	}

	private Cluster(){
		GPUS = new ConcurrentLinkedDeque<>();
		CPUS = new ConcurrentLinkedDeque<>();
	}

}
