package e2HelloAkkaMore;

import akka.actor.UntypedActor;

public class GreeterActor extends UntypedActor {

    public static enum Msg {
        GREET, DONE;
    }

    @Override
    public void onReceive(Object msg) {
        if (msg == Msg.GREET) {
            System.out.println("Hello World!");
            // getSender() 是获取发送消息的 actor.tell(a,b) 的第二个参数的
            getSender().tell(Msg.DONE, getSelf());
        } else
            unhandled(msg);
    }

}
