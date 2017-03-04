package e3Statistics.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import e3Statistics.message.MapData;
import e3Statistics.message.WordCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by xuhuaiyu on 2017/2/21.
 */
public class MapActor extends UntypedActor {

    private ActorRef reduceActor = null;

    private String[] IGNORE_WORDS = {"a","is"};

    private List<String> IGNORE_WORDS_LIST = Arrays.asList(IGNORE_WORDS);

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String) {
            String workSource = (String) message;
            // 把句子拆解成单词，放到MapData中
            MapData mapData = map(workSource);
            // 发送MapData到reduceActor
            reduceActor.tell(mapData, ActorRef.noSender());
        } else {
            unhandled(message);
        }
    }

    private MapData map(String line) {
        List<WordCount> dataList = new ArrayList<>();
        StringTokenizer parser = new StringTokenizer(line);
        while(parser.hasMoreElements()) {
            String word = parser.nextToken().toLowerCase();
            if(!IGNORE_WORDS_LIST.contains(word)) {
                dataList.add(new WordCount(word, 1));
            }
        }
        return new MapData(dataList);
    }

    public MapActor(ActorRef reduceActor) {
        this.reduceActor = reduceActor;
    }

    // properties
    public ActorRef getReduceActor() {
        return reduceActor;
    }

    public void setReduceActor(ActorRef reduceActor) {
        this.reduceActor = reduceActor;
    }
}
