package cn.sheeva.swap;

import java.util.HashMap;

public class PostingList {
    public PostingList(long df,HashMap<Long, Long> list) {
        this.df=df;
        this.list=list;
    }
    
    private long df;
    //docid & tf
    private HashMap<Long, Long> list=new HashMap<>();
    public long getDf() {
        return df;
    }
    public void setDf(long df) {
        this.df = df;
    }
    public HashMap<Long, Long> getList() {
        return list;
    }
    public void setList(HashMap<Long, Long> list) {
        this.list = list;
    }
    
    
}
