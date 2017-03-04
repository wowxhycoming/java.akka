package e9Future;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.transactor.Coordinated;

/**
 * Created by xuhuaiyu on 2017/3/1.
 */
public class PrintActor extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object o) throws Throwable {
        log.info("PrintActor.onReceive:" + o);
        if (o instanceof Integer) {
            log.info("print:" + o);
        } else if(o instanceof Coordinated) {
            Coordinated coordinated = (Coordinated) o;
            int downCount = (int) coordinated.getMessage();
            log.info("print:" + downCount);
        } else {
            unhandled(o);
        }
    }

}
