package e9Future;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by xuhuaiyu on 2017/3/1.
 */
public class WorkerActor extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object o) throws Throwable {
        log.info("WorkerActor.onReceive:" + o + ", WorkerActor 的 hashCode ： " + this.toString());

        if (o instanceof Integer) {
            Thread.sleep(5000); // 模拟计算时间很长
            int i = Integer.parseInt(o.toString());
            getSender().tell(i*i, getSelf());
        } else {
            unhandled(o);
        }
    }

}
