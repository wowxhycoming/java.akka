package e4Lifecycle;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by xuhuaiyu on 2017/2/23.
 */
public class WatchActor extends UntypedActor {

    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    /**
     * 监听一个actor
     *
     * @param actorRef
     */
    public WatchActor(ActorRef actorRef) {
        getContext().watch(actorRef);
    }

    @Override
    public void onReceive(Object msg) throws InterruptedException {
        if (msg == MyWork.Msg.WORKING) {
            logger.info("i watched worker working");
        }
        if (msg == MyWork.Msg.DONE) {
            logger.info("i watched worker done");
        }

        // 当被关注的 actor 关闭， 关注他的 actor 会收到 Terminated
        if (msg instanceof Terminated) {
            //这里简单打印一下，然后停止system
            logger.error(((Terminated) msg).getActor().path() + " has Terminated. now shutdown the system");
            getContext().system().shutdown();
        } else {
            unhandled(msg);
        }



    }

}
