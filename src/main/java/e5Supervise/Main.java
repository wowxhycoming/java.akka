package e5Supervise;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;

/**
 * Created by xuhuaiyu on 2017/2/27.
 *
 * 监督
 */
public class Main {

    public static void main(String[] args) {
        /*
        OneForOneStrategy : 只对出问题的子actor进行处理. 这是默认策略
        AllForOneStrategy : 对子actor以及他的所有兄弟actor进行处理
         */
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef superVisor = system.actorOf(Props.create(SuperVisor.class), "superVisor");
        superVisor.tell(Props.create(RestartActor.class), ActorRef.noSender()); // 会在 superVisor 中创建 restartActor

        // 面对成千上万的actor，可以通过 actorSelection 方便的选择 actor 进行消息投递
        // 其支持通配符匹配 getContext().actorSelection("/user/worker_*")
        ActorSelection actorSelection = system.actorSelection("akka://strategy/user/superVisor/restartActor");// 这是akka的路径。restartActor 是在 SuperVisor 中创建的。

        for (int i = 0; i < 100; i++) {
            actorSelection.tell(RestartActor.Msg.RESTART, ActorRef.noSender());
        }
    }

}
