package e2statistics;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import e2statistics.actor.MasterActor;
import e2statistics.message.Result;

/**
 * Created by xuhuaiyu on 2017/2/21.
 */
public class Statistics {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem statisticsSystem = ActorSystem.create("statistics");
        ActorRef masterActor = statisticsSystem.actorOf(Props.create(MasterActor.class), "masterActor");

        masterActor.tell("Hi ! My name is akka, I'm happy to be here. ", ActorRef.noSender());
        masterActor.tell("Hi ! My name is akka, I'm so happy to be here. ", ActorRef.noSender());
        masterActor.tell("Hi ! My name is akka, I'm very happy to be here. I almost fly", ActorRef.noSender());

        Thread.sleep(500);

        masterActor.tell(new Result(), ActorRef.noSender());

        Thread.sleep(500);

        statisticsSystem.shutdown();

    }
}
