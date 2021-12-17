package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Model;

import java.util.List;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConferenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    ConfrenceInformation confrenceInformation;
    private int ticks;

    public ConferenceService(ConfrenceInformation confrenceInformation) {
        super("conferenceService");
        this.confrenceInformation = confrenceInformation;
        this.ticks = 0;
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        subscribeBroadcast(TickBroadcast.class,callback->{
            ticks++;
            if(ticks == this.confrenceInformation.getDate()){
                sendBroadcast(new PublishConferenceBroadcast(this.confrenceInformation.getModels()));
                this.terminate();
            }
        });
        subscribeEvent(PublishResultsEvent.class,callback->{
            confrenceInformation.addModels(callback.getModel());
        });

    }
}
