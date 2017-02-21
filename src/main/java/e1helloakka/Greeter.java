package e1helloakka;

import akka.actor.UntypedActor;

/**
 * Created by xuhuaiyu on 2017/2/21.
 */
public class Greeter extends UntypedActor{

    public void onReceive(Object o) throws Throwable {
        System.out.println("hello " + o);
    }
}
