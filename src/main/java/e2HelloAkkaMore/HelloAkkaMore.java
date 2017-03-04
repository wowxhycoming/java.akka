package e2HelloAkkaMore;

/**
 * Created by xuhuaiyu on 2017/2/21.
 * <p>
 * 再来一个Hello Akka
 * 通过 Akka 核心启动
 */
public class HelloAkkaMore {

    public static void main(String[] args) {
        // akka 不是变量，是包名。
        akka.Main.main(new String[]{MasterActor.class.getName()});
    }
}
