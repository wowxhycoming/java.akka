package e3Statistics.message;

import java.util.List;

/**
 * Created by xuhuaiyu on 2017/2/21.
 */
public class MapData {

    private List<WordCount> dataList;

    public List<WordCount> getDataList() {
        return dataList;
    }

    public void setDataList(List<WordCount> dataList) {
        this.dataList = dataList;
    }

    public MapData(List<WordCount> dataList) {
        this.dataList = dataList;
    }
}
