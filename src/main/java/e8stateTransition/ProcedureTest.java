package e8stateTransition;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import com.typesafe.config.ConfigFactory;

/**
 * Created by xuhuaiyu on 2017/2/27.
 *
 * 内置状态转换
 *
 * 在 actor 运行过程中，可能会有多种状态，各个状态间可能会存在切换的情况，akka 已经帮我们考虑到这种情况情况的处理: Procedure.
 *
 * 下面模拟一个婴儿。婴儿有两种不同的状态，开心和生气，婴儿有个特点就是好玩，永远不会累，所以让其睡觉婴儿就会生气，让他继续玩就会很高兴。
 */
public class ProcedureTest extends UntypedActor {

    private enum Msg {PLAY, SLEEP}

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    Procedure<Object> happy = new Procedure<Object>() {
        @Override
        public void apply(Object o) throws Exception {
            log.info("i am happy! " + o);
            if (o == Msg.PLAY) {
                getSender().tell("i am already happy!!", getSelf());
                log.info("i am already happy!!");
            } else if (o == Msg.SLEEP) {
                log.info("i do not like sleep!");
                getContext().become(angry);
            } else {
                unhandled(o);
            }
        }
    };

    Procedure<Object> angry = new Procedure<Object>() {
        @Override
        public void apply(Object o) throws Exception {
            log.info("i am angry! "+o);
            if(o ==Msg.SLEEP){
                getSender().tell("i am already angry!!", getSelf());
                log.info("i am already angry!!");
            } else if(o ==Msg.PLAY) {
                log.info("i like play.");
                getContext().become(happy);
            } else {
                unhandled(o);
            }
        }
    };


    @Override
    public void onReceive(Object o) throws Throwable {
        log.info("onReceive msg: " + o);
        if(o == Msg.SLEEP){
            getContext().become(angry);
        }else if(o == Msg.PLAY){
            getContext().become(happy);
        }else {
            unhandled(o);
        }

    }



    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef procedureTest = system.actorOf(Props.create(ProcedureTest.class), "ProcedureTest");

        // TODO onReceive 方法只被调用了一次 （被调用的 actor 中只要执行一次 getContext().become(object) 后， 再发起 tell 就不会触发 onReceive ）
        procedureTest.tell(Msg.PLAY, ActorRef.noSender());
        procedureTest.tell("1", ActorRef.noSender());
        procedureTest.tell(Msg.SLEEP, ActorRef.noSender());
        procedureTest.tell("2", ActorRef.noSender());
        procedureTest.tell(Msg.PLAY, ActorRef.noSender());
        procedureTest.tell("3", ActorRef.noSender());
        procedureTest.tell(Msg.PLAY, ActorRef.noSender());
        procedureTest.tell("4", ActorRef.noSender());

//        procedureTest.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
