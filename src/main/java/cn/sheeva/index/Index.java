package cn.sheeva.index;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import cn.sheeva.index.InvertIndex.InvertList;
import cn.sheeva.swap.Doc;
import cn.sheeva.swap.PostingList;
import cn.sheeva.util.Formula;
import cn.sheeva.util.token.SimpleTokenizer;
import cn.sheeva.util.token.Tokenizer;

public class Index {
        
    private final Tokenizer tokenizer=new SimpleTokenizer();
    
    private DocMap docMap=new DocMap();
    private InvertIndex invertIndex=new InvertIndex();

    
    /**
     * 添加文档
     * @createTime：2017年5月2日 
     * @author: gaojiabao
     */
    public void add(String text){
        //generate docid
        //new doc
        //add doc to docmap
        //add doc to invertindex
        long id=DocIdGenerator.generate();
        Doc doc=new Doc(id, text);
        docMap.add(doc);
        
        List<String> tokens=tokenizer.getTokens(text);
        invertIndex.add(id,tokens);
    }
    
    /**
     * 获取某个token的倒排列表
     * @createTime：2017年5月2日 
     * @author: gaojiabao
     */
    public PostingList getPostingList(String token){
        InvertList invertList=invertIndex.getInvertList(token);
        PostingList postingList=invertList.convert2PostingList();
        
        
        return postingList;
    }
    

    
    
    /**
     * 获得索引中的所有token
     * @createTime：2017年5月2日 
     * @author: gaojiabao
     */
    public Set<String> getAllTokens(){
        return invertIndex.getAllTokens();
    }
    
    public long getDocNum(){
        return docMap.getDocNum();
    }
    
    
    public Doc getDoc(long id){
        return docMap.get(id);
    }
    
    
    /**
     * 获取所有文档的欧式长度
     * @createTime：2017年5月3日 
     * @author: gaojiabao
     */
    public Map<Long, Double> getDocsOLength(){
        long docNum=this.getDocNum();
        Map<Long, Double> docsLength=new HashMap<>();        
        
        Set<String> allTokens=this.getAllTokens();
        for (String token : allTokens) {
            InvertIndex.InvertList invertList=invertIndex.getInvertList(token);
            long df=invertList.getDf();
            Map<Long,Long> tfs=invertList.getList();
            for (Entry<Long, Long> entry : tfs.entrySet()) {
                long docid=entry.getKey();
                long tf=entry.getValue();
                
                Double length=docsLength.get(docid);
                length=length==null?0:length;
                
                double tf_idf=Formula.tf_idf(tf, docNum, df);
                length+=(tf_idf*tf_idf);
                
                docsLength.put(docid, length);
            }
        }
        
        for (long docid : docsLength.keySet()) {
            double length=docsLength.get(docid);
            docsLength.put(docid, Math.sqrt(length));
        }
        
        return docsLength;
    }
    

}
