package e1helloakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Created by xuhuaiyu on 2017/2/21.
 */
public class HelloAkka {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("HelloAkka");

        ActorRef greeterActor = actorSystem.actorOf(Props.create(Greeter.class), "greeterActor");

        greeterActor.tell("xhy", ActorRef.noSender());

        actorSystem.shutdown();
    }


}
