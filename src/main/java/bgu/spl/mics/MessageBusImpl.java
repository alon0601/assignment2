package bgu.spl.mics;

import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static MessageBusImpl messageBusInstance = null;
	private Map<MicroService,BlockingDeque<Message>> microServiceMessages;
	private Map<Class<? extends Event>, BlockingDeque<MicroService>> eventQueue;
	private Map<Class<? extends Broadcast>, BlockingDeque<MicroService>> broadcastQueue;
	private Map<Event,Future> futureQueue;

	final Object eventQueueLock = new Object();
	final Object microServiceMessagesLock = new Object();
	final Object broadcastQueueLock = new Object();
	final Object futureQueueLock = new Object();



	private MessageBusImpl(){
		microServiceMessages = new ConcurrentHashMap<>();
		eventQueue = new ConcurrentHashMap<>();
		broadcastQueue = new ConcurrentHashMap<>();
		futureQueue = new ConcurrentHashMap<>();
	}

    public static MessageBusImpl getInstance() {
    	if(messageBusInstance == null) {
			messageBusInstance = new MessageBusImpl();
		}
    	return messageBusInstance;
    }

    @Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

		synchronized (type) {
			if (eventQueue.get(type) == null)
				eventQueue.put(type, new LinkedBlockingDeque<>());

			BlockingDeque<MicroService> microServices = eventQueue.get(type);
			microServices.add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {

		synchronized (type) {
			if (broadcastQueue.get(type) == null)
				broadcastQueue.put(type, new LinkedBlockingDeque<>());
			BlockingDeque<MicroService> microServices = broadcastQueue.get(type);
			microServices.add(m);

		}

	}

	@Override
	public <T> void complete(Event<T> e, T result) {

		//should we lock this?

		futureQueue.get(e).resolve(result);
		futureQueue.remove(e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (b.getClass()){
			BlockingDeque<MicroService> microServices = broadcastQueue.get(b.getClass());
			for(MicroService m:microServices){
				try {
					microServiceMessages.get(m).put(b);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		synchronized (e.getClass()) {
			if (eventQueue.get(e.getClass()) != null) {
				BlockingDeque<MicroService> micros = eventQueue.get(e.getClass());
				MicroService m = micros.poll();
				Future<T> future = new Future<T>();
				microServiceMessages.get(m).add(e);
				futureQueue.put(e, future);
				micros.addLast(m);
				return future;
			}
		}
		return null;
	}

	@Override
	public void register(MicroService m) {
		microServiceMessages.put(m,new LinkedBlockingDeque<>());
	}

	@Override
	public void unregister(MicroService m) {
		for (Class<? extends Event> type : eventQueue.keySet()) {
			synchronized (type){
				eventQueue.get(type).remove(m);
			}
		}
		for (Class<? extends Broadcast> type : broadcastQueue.keySet()) {
			synchronized (type){
				broadcastQueue.get(type).remove(m);
			}
		}
		microServiceMessages.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		if(microServiceMessages.get(m) == null)
			throw new IllegalStateException();
		return microServiceMessages.get(m).takeFirst();
	}

	

}
