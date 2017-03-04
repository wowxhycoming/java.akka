package e2HelloAkkaMore;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class MasterActor extends UntypedActor {

    @Override
    public void preStart() {
        // 父 Actor 创建的子 Actor
        final ActorRef greeter = getContext().actorOf(Props.create(GreeterActor.class), "greeter");
        // 向子 Actor 发送消息。
        // region // 发送消息
        /**
         * 发送消息： 消息不能直接到达目标 Actor ， 只能通过代理对象。
         * 1. 先拿到目标 Actor 的代理对象： ActorRef
         * 2. ActorRef 会将消息发送给 Dispatcher
         * 3. Dispatcher 将 message 按照顺序保存到目标 Actor 的 MailBox 中
         * 4. Dispatcher 将 Mailbox 放在一个Thread 中
         * 5. MailBox 按照队列顺序取出消息，并最终将它递给真实的 Actor 接受方法中
         */
        // endregion
        // region // MailBox
        /**
         * MailBox
         * 每个 Actor 都有一个 MailBox（有一个特殊情况）。当然 ReceiveActor 也有一个 MailBox。
         * ReceiveActor 需要检查 MailBox 并处理其中的 message。
         * MailBox 中有个队列并以 FIFO 方式储存和处理消息。
         */
        // endregion
        // region // Dispatcher
        /**
         * Dispatcher
         * Dispatcher 包装了一个 ExecutorService (ForkJoinPool 或者 ThreadPoolExecutor).它通过 ExecutorService 运行 MailBox
         * Mailbox 必须是一个线程
         *
         * 当 MailBox 的 run 方法被运行，它将从队列中取出消息，并传递到 Actor 进行处理。
         * 该方法最终在你将消息 tell 到 ActorRef 中的时候被调用，在目标 Actor 其实是个 receive 方法。
         */
        // endregion
        greeter.tell(GreeterActor.Msg.GREET, getSelf());
//        greeter.tell(GreeterActor.Msg.GREET, ActorRef.noSender());
    }

    @Override
    public void onReceive(Object msg) {
        if (msg == GreeterActor.Msg.DONE) {
            // 停止当前 Actor 并停止 ActorSystem
            getContext().stop(getSelf());
        } else
            unhandled(msg);
    }
}
