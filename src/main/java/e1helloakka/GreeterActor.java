package e1HelloAkka;

import akka.actor.UntypedActor;

/**
 * Created by xuhuaiyu on 2017/2/21.
 */
public class GreeterActor extends UntypedActor{

    @Override
    public void preStart() {
        // 创建该 actor 就执行
        System.out.println("actor starting");
    }

    // 这个 Actor 没有返回 或者 发送 消息
    public void onReceive(Object o) throws Throwable {
        System.out.println("hello " + o);
    }
}
