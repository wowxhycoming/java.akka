package e2HelloAkkaMore;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by xuhuaiyu on 2017/2/21.
 * <p>
 * 再来一个Hello Akka
 * 通过创建 actor 方式启动
 */
public class HelloAkkaMore2 {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("HelloAkkaMore");
        ActorRef masterActor = system.actorOf(Props.create(MasterActor.class), "masterActor");
        // 介绍 masterActor 给 Terminator
        system.actorOf(Props.create(Terminator.class, masterActor), "terminator");
    }

    public static class Terminator extends UntypedActor {

        private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
        private final ActorRef ref;

        public Terminator(ActorRef ref) {
            this.ref = ref;
            getContext().watch(ref);
        }

        @Override
        public void onReceive(Object msg) {
            if (msg instanceof Terminated) {
                log.info("{} has terminated, shutting down system", ref.path());
                getContext().system().terminate();
            } else {
                unhandled(msg);
            }
        }

    }
}
