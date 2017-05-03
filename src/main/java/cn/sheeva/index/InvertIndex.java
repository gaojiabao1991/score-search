package cn.sheeva.index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import cn.sheeva.swap.Doc;
import cn.sheeva.swap.PostingList;
import cn.sheeva.util.token.SimpleTokenizer;
import cn.sheeva.util.token.Tokenizer;

public class InvertIndex {

    //token&invertList
    private HashMap<String, InvertList> map = new HashMap<>();

    protected void add(long docid, List<String> tokens) {

        for (String token : tokens) {
            InvertList invertList = map.get(token);
            if (invertList == null) {
                invertList = new InvertList();
                map.put(token, invertList);
            }

            invertList.add(docid);
        }
    }

    protected Set<String> getAllTokens() {
        return new HashSet<>(map.keySet());
    }
    
    protected InvertList getInvertList(String token) {
        return map.get(token);
    }
    
    protected static class InvertList {
        private long df;
        //docid & tf
        private HashMap<Long, Long> list = new HashMap<>();

        protected void add(long docId) {
            if (list.containsKey(docId)) {
                long tf = list.get(docId);
                list.put(docId, ++tf);
            } else {
                list.put(docId, 1l);
                df++;
            }
        }
        
        protected PostingList convert2PostingList() {
            return new PostingList(df, list);
        }

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
        
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }
    
}
