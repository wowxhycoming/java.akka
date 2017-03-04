package e3Statistics.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import e3Statistics.message.Result;

/**
 * Created by xuhuaiyu on 2017/2/21.
 */
public class MasterActor extends UntypedActor{

    private ActorRef aggregateActor = getContext().actorOf(Props.create(AggregateActor.class), "aggregateActor");
    private ActorRef reduceActor = getContext().actorOf(Props.create(ReduceActor.class, aggregateActor), "reduceActor");
    private ActorRef mapActor = getContext().actorOf(Props.create(MapActor.class, reduceActor), "mapActor");




    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String) {
            mapActor.tell(message, ActorRef.noSender());
        } else if(message instanceof Result) {
            aggregateActor.tell(message, ActorRef.noSender());
        } else {
            unhandled(message);
        }
    }
}
