package e3Statistics.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import e3Statistics.message.MapData;
import e3Statistics.message.ReduceData;
import e3Statistics.message.WordCount;

import java.util.HashMap;
import java.util.List;

/**
 * Created by xuhuaiyu on 2017/2/21.
 */
public class ReduceActor extends UntypedActor{

    private ActorRef aggregateActor = null;

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof MapData) {
            MapData mapData = (MapData) message;
            ReduceData reduceData = reduce(mapData);
            aggregateActor.tell(reduceData, ActorRef.noSender());
        } else {
            unhandled(message);
        }
    }

    private ReduceData reduce(MapData mapData) {
        HashMap<String, Integer> reduceMap = new HashMap<>();
        List<WordCount> wordCountList = mapData.getDataList();
        for(WordCount wordCount : wordCountList) {
            if(reduceMap.containsKey(wordCount.getWord())) {
                Integer value = reduceMap.get(wordCount.getWord());
                value++;
                reduceMap.put(wordCount.getWord(), value);
            } else {
                reduceMap.put(wordCount.getWord(), 1);
            }
        }

        return new ReduceData(reduceMap);
    }

    public ReduceActor(ActorRef aggregateActor) {
        this.aggregateActor = aggregateActor;
    }

    public ActorRef getAggregateActor() {
        return aggregateActor;
    }

    public void setAggregateActor(ActorRef aggregateActor) {
        this.aggregateActor = aggregateActor;
    }
}
