package e5Supervise;

import akka.actor.UntypedActor;
import scala.Option;

/**
 * Created by xuhuaiyu on 2017/2/27.
 */

public class RestartActor extends UntypedActor {

    public  enum  Msg{
        DONE, RESTART;
    }


    @Override
    public void preStart() throws Exception {
        System.out.println("preStart    hashCode=" + this.hashCode());
    }
    @Override
    public void postStop() throws Exception {
        System.out.println("postStop    hashCode=" + this.hashCode());
    }



    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        System.out.println("preRestart    hashCode=" + this.hashCode());
    }
    @Override
    public void postRestart(Throwable reason) throws Exception {
        super.postRestart(reason);
        System.out.println("postRestart    hashCode=" + this.hashCode());
    }



    @Override
    public void onReceive(Object o) throws Throwable {
        if(o == Msg.DONE) {
            getContext().stop(getSelf());
        } else if(o == Msg.RESTART) {
            // 下面一行抛出未捕获异常， 处理 NullPointerException 为 SupervisorStrategy.restart()
            System.out.println(((Object) null).toString());

            // 下面一行抛出未捕获异常， 默认会被restart，但这里处理 ArithmeticException 为 SupervisorStrategy.resume()
//            double a = 1/0;
        } else {
            unhandled(o);
        }

    }
}
