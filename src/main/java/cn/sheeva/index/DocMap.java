package cn.sheeva.index;

import java.util.HashMap;
import java.util.Set;

import cn.sheeva.swap.Doc;

public class DocMap {
    private HashMap<Long, Doc> map=new HashMap<>();
    
    protected void add(Doc doc){
        map.put(doc.getId(), doc);
    }
    
    protected Doc get(long id){
        return map.get(id);
    }
    
    protected long getDocNum(){
        return map.size();
    }
    
    /**
     * view of DocMap, just used for read
     * @createTime：2017年5月2日 
     * @author: gaojiabao
     */
    protected HashMap<Long, Doc> getDocMap() {
        return map;
    }
}
