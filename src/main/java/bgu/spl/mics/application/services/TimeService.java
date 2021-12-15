package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	private static TimeService timeService = null;
	private int tickTime;
	private int currentTime;
	private int duration;

//	public static TimeService getInstance(){
//		if (timeService == null)
//			timeService = new TimeService();
//		return timeService;
//	}

	private TimeService(int tickTime,int duration) {
		super("Time Service");
		this.tickTime = tickTime;
		this.duration = duration;
		currentTime = 1;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	private void tick(){
		if (currentTime >= duration){
			this.closeProgram();
		}
		else{
			currentTime++;
			this.sendBroadcast(new TickBroadcast());
		}
	}

	private void closeProgram(){ // do this later

	}

	@Override
	protected void initialize() {
		Timer timer = new Timer();
		TimerTask tick =new TimerTask() {
			@Override
			public void run() {
				tick();
			}
		};
		timer.schedule(tick,0,tickTime);
	}

}
