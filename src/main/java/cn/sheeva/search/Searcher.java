
package cn.sheeva.search;

import java.text.Normalizer.Form;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import cn.sheeva.index.Index;
import cn.sheeva.swap.Doc;
import cn.sheeva.swap.PostingList;
import cn.sheeva.util.Formula;
import cn.sheeva.util.token.SimpleTokenizer;
import cn.sheeva.util.token.Tokenizer;

public class Searcher {
    public Searcher(Index index) {
        this.index=index;
        this.allTokens=index.getAllTokens();
    }
    
    private Tokenizer tokenizer=new SimpleTokenizer();
    private final Index index;  //index won't change
    private final Set<String> allTokens;  //allTokens won't change
    
    
    public List<Doc> search(String query){
        List<String> queryTokens=tokenizer.getTokens(query);
        Map<String, Integer> queryWordBag = getQueryWordBag(queryTokens);
        Set<String> queryTokenSet=new HashSet<>(queryTokens);
        
        long docNum=index.getDocNum();
        //docid&score，先计算内积
        Map<Long, Double> dotProducts = getDotProducts(queryWordBag, queryTokenSet, docNum);
        
        //所有文档的欧式长度
        Map<Long, Double> docsOLength=index.getDocsOLength();
        
        //查询的欧式长度
        double queryOLength = getQueryOLength(queryWordBag, docNum);

        //生成最终得分
        for (Entry<Long, Double> entry : dotProducts.entrySet()) {
            long docid=entry.getKey();
            double dotProduct=entry.getValue();
            
            double score=dotProduct
                    /queryOLength
                    /docsOLength.get(docid);
            
            dotProducts.put(docid, score);
        }
        
        //按得分排序并返回前N条
        final int N=10;
        List<Long> ids = getTopNIds(dotProducts, N);
        
        List<Doc> docs=getTopNDocs(ids);
               
        return docs;
    }
    
    private List<Doc> getTopNDocs(List<Long> ids){
        List<Doc> docs=new LinkedList<>();
        for (Long id : ids) {
            docs.add(index.getDoc(id));
        }
        return docs;
    }

    private List<Long> getTopNIds(Map<Long, Double> dotProducts, final int N) {
        Comparator<Entry<Long, Double>> comparator=new Comparator<Entry<Long, Double>>() {
            @Override
            public int compare(Entry<Long, Double> o1, Entry<Long, Double> o2) {
                if (o1.getValue()>o2.getValue()) {
                    return -1;
                }else if (o1.getValue()<o2.getValue()) {
                    return 1;
                }else {
                    return 0;  //相等；double只能在精度内比较相等，可能有误判，不过排序的精度不要求那么高
                }
            }
            
        };
        
        PriorityQueue<Entry<Long, Double>> q=new PriorityQueue<>(comparator);
        //将匹配文档入队列
        for (Entry<Long, Double> entry : dotProducts.entrySet()) {
            q.add(entry);
        }
        
        List<Long> ids=new LinkedList<>();
        Entry<Long, Double> entry=null;
        while (ids.size()<N&&(entry=q.poll())!=null) {
            ids.add(entry.getKey());
        }
        return ids;
    }

    private double getQueryOLength(Map<String, Integer> queryWordBag, long docNum) {
        double queryOLength=0d;
        for (Entry<String, Integer> entry : queryWordBag.entrySet()) {
            String token=entry.getKey();
            Integer tf=entry.getValue();
            long df=index.getPostingList(token).getDf();
            
            double tf_idf=Formula.tf_idf(tf, docNum, df);
            queryOLength+=(tf_idf*tf_idf);
        }
        queryOLength=Math.sqrt(queryOLength);
        return queryOLength;
    }

    private Map<Long, Double> getDotProducts(Map<String, Integer> queryWordBag, Set<String> queryTokenSet, long docNum) {
        //docid&score
        Map<Long, Double> dotProducts=new HashMap<>();
        for (String queryToken : queryTokenSet) {
            PostingList postingList=index.getPostingList(queryToken);
            long df=postingList.getDf();
            long qtf=queryWordBag.get(queryToken);
            
            for (Entry<Long, Long> entry : postingList.getList().entrySet()) {
                long docid=entry.getKey();
                long dtf=entry.getValue();
                
                Double dotProduct=dotProducts.get(docid);
                dotProduct=dotProduct==null?0d:dotProduct;
                
                double q_tf_idf=Formula.tf_idf(qtf, docNum, df);
                double d_tf_idf=Formula.tf_idf(dtf, docNum, df);
                dotProduct+=(q_tf_idf*d_tf_idf);
                
                dotProducts.put(docid, dotProduct);
            }
        }
        return dotProducts;
    }

    private Map<String, Integer> getQueryWordBag(List<String> tokens) {
        Map<String, Integer> queryWordBag=new HashMap<>();
        for (String token : allTokens) {
            queryWordBag.put(token, 0);
        }
        
        for (String token : tokens) {
            Integer tf=queryWordBag.get(token);
            if (tf!=null) {
                queryWordBag.put(token, ++tf);
            }else {
                //token not in allTokens,ignore
            }
        }
        return queryWordBag;
    }
    
    
}
    