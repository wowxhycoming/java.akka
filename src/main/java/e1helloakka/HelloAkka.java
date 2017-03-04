package e1HelloAkka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Created by xuhuaiyu on 2017/2/21.
 *
 * 一个简单的Hello Akka
 *
 * "/user"是所有由用户创建的顶级actor的监管者，用ActorSystem.actorOf创建的actor在其下一个层次 are found at the next level。
 * "/system" 是所有由系统创建的顶级actor（如日志监听器或由配置指定在actor系统启动时自动部署的actor）的监管者。
 * "/deadLetters" 是死信actor，所有发往已经终止或不存在的actor的消息会被送到这里。
 * "/temp"是所有系统创建的短时actor(i.e.那些用在ActorRef.ask的实现中的actor)的监管者。
 * "/remote" 是一个人造的路径，用来存放所有其监管者是远程actor引用的actor。
 */
public class HelloAkka {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("HelloAkka");

        //  通过 ActorSystem 创建 Actor 获得其引用 , 创建 actor 的过程是异步的
        ActorRef greeterActor = actorSystem.actorOf(Props.create(GreeterActor.class), "greeterActor");
        System.out.println("actor created");

        // 通过引用向 Actor 发送消息
        greeterActor.tell("Akka", ActorRef.noSender());

        actorSystem.shutdown();
    }


}
