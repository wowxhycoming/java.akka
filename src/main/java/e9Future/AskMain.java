package e9Future;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuhuaiyu on 2017/3/1.
 */
public class AskMain {

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef printActor = system.actorOf(Props.create(PrintActor.class), "PrintActor");
        ActorRef workerActor = system.actorOf(Props.create(WorkerActor.class), "WorkerActor");

        // 异步
        Future<Object> future = Patterns.ask(workerActor, 5, 6000);
        Future<Object> future1 = Patterns.ask(workerActor, 8, 12000);
        System.out.println("异步调用，不需要同步等待 Future 执行完成");
        System.out.println("但是为什么两个 Future<Object> future = Patterns.ask() 是同步的？");
        System.out.println("在 Worker 中打印了 hashcode ，是相等的，难道是因为 onReceive 是同步的 ？ 按收到消息的顺序处理消息？");

        // 同步 ： 等待 future 返回
        int result = (int) Await.result(future, Duration.create(6, TimeUnit.SECONDS));
        System.out.println("result:" + result);

        // 异步 ： 不等待返回值，直接重定向到其他 actor，有返回值来的时候将会重定向到 printActor
        Patterns.pipe(future1, system.dispatcher()).to(printActor);
        // 因为是异步，直接执行下一行
        System.out.println("在返回结果之前打印");


        workerActor.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
