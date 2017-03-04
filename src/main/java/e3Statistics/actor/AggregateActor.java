package e3Statistics.actor;

import akka.actor.UntypedActor;
import e3Statistics.message.ReduceData;
import e3Statistics.message.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuhuaiyu on 2017/2/21.
 */
public class AggregateActor extends UntypedActor {

    private Map<String, Integer> finalReduceMap = new HashMap<>();

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof ReduceData) {
            ReduceData reduceData = (ReduceData) message;
            aggregate(reduceData);
        } else if(message instanceof Result) {
            System.out.println(finalReduceMap.toString());
        } else {
            unhandled(message);
        }
    }

    public void aggregate(ReduceData reduceData) {
        Integer count = null ;
        for(String key : reduceData.getReduceData().keySet()) {
            if(finalReduceMap.containsKey(key)) {
                count = reduceData.getReduceData().get(key) + finalReduceMap.get(key);
                finalReduceMap.put(key, count);
            } else {
                finalReduceMap.put(key, reduceData.getReduceData().get(key));
            }
        }
    }
}
