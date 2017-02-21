package e2statistics.message;

import java.util.HashMap;

/**
 * Created by xuhuaiyu on 2017/2/21.
 */
public class ReduceData {

    private HashMap<String, Integer> reduceData;

    public ReduceData(HashMap<String, Integer> reduceData) {
        this.reduceData = reduceData;
    }

    public HashMap<String, Integer> getReduceData() {
        return reduceData;
    }

    public void setReduceData(HashMap<String, Integer> reduceData) {
        this.reduceData = reduceData;
    }
}
