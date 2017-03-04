package e4Lifecycle;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * Created by xuhuaiyu on 2017/2/23.
 *
 * 生命周期
 * preStart：在actor实例化后执行，重启时不会执行。
 * postStop：在actor正常终止后执行，异常重启时不会执行。
 * preRestart：在actor异常重启前保存当前状态。
 * postRestart：在actor异常重启后恢复重启前保存的状态。当异常引起了重启，新actor的postRestart方法被触发，默认情况下preStart方法被调用。
 */
public class Main {

    public static void main(String[] args) {
        // 创建 ActorSystem。一般来说，一个系统只需要一个 ActorSystem。
        // 参数1：系统名称。参数2：配置文件
        ActorSystem system = ActorSystem.create("Lifecycle", ConfigFactory.load("akka.config"));
        // actor 的创建是异步的
        ActorRef myWork = system.actorOf(Props.create(MyWork.class), "MyWork");
        System.out.println("myWork created");
        ActorRef watchActor = system.actorOf(Props.create(WatchActor.class, myWork), "WatchActor");

        myWork.tell(MyWork.Msg.WORKING, ActorRef.noSender());
        myWork.tell(MyWork.Msg.DONE, ActorRef.noSender());

        //中断myWork
        myWork.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}

/**
 * Restart
 * 1.过程：
 * 要被重启的actor的preRestart被调用，携带着导致重启的异常以及触发异常的消息； 如果重启并不是因为消息的处理而发生的，所携带的消息为None，
 *     例如，当一个监管者没有处理某个异常继而被它自己的监管者重启时。 这个方法是用来完成清理、准备移交给新的actor实例的最佳位置。它的缺省实现是终止所有的子actor并调用postStop。
 * 最初actorOf调用的工厂方法将被用来创建新的实例。
 * 新的actor的postRestart方法被调用，携带着导致重启的异常信息。
 * actor的重启会替换掉原来的actor对象；重启不影响邮箱的内容, 所以对消息的处理将在postRestart hook返回后继续。触发异常的消息不会被重新接收。
 *     在actor重启过程中所有发送到该actor的消息将象平常一样被放进邮箱队列中。
 *
 * 2.重启策略的详细内容：
 * actor被挂起
 * 调用旧实例的 supervisionStrategy.handleSupervisorFailing 方法 (缺省实现为挂起所有的子actor)
 * 调用preRestart方法，从上面的源码可以看出来，preRestart方法将所有的children Stop掉了，并调用postStop回收资源
 * 调用旧实例的supervisionStrategy.handleSupervisorRestarted方法(缺省实现为向所有剩下的子actor发送重启请求)
 * 等待所有子actor终止直到 preRestart 最终结束
 * 再次调用之前提供的actor工厂创建新的actor实例
 * 对新实例调用 postRestart
 * 恢复运行新的actor
 */