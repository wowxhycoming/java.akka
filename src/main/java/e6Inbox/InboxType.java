package e6Inbox;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by xuhuaiyu on 2017/2/27.
 *
 * inbox 使用inbox消息收件箱来给某个 actor 发消息，并且可以进行交互
 */
public class InboxType extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public enum Msg{
        WORKING, DONE, CLOSE
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        if(o == Msg.WORKING){
            log.info("i am working.");
        }else if(o == Msg.DONE){
            log.info("i am done");
        }else if(o == Msg.CLOSE){
            log.info("i am close.");
            getSender().tell(Msg.CLOSE, getSelf()); // 告诉消息发送者我要关闭了。
            getContext().stop(getSelf());// 关闭自己
        }else{
            unhandled(o);
        }
    }

    public static void main(String [] args){
        ActorSystem system = ActorSystem.create("inbox", ConfigFactory.load("akka.conf"));
        ActorRef inboxType = system.actorOf(Props.create(InboxType.class), "inboxType");

        Inbox inbox = Inbox.create(system);
        inbox.watch(inboxType); // 监听一个actor

        // 通过inbox来发送消息
        inbox.send(inboxType, Msg.WORKING);
        inbox.send(inboxType, Msg.DONE);
        inbox.send(inboxType, Msg.CLOSE);

        while(true){
            try {
                Object receive = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
                if(receive == Msg.CLOSE){// 收到的inbox的消息
                    System.out.println("inboxTypeActor is closing");
                } else if(receive instanceof Terminated){ // 中断 ，和线程一个概念
                    System.out.println("inboxTypeActor is closed");
                    system.shutdown();
                    break;
                } else {
                    System.out.println(receive);
                }
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
