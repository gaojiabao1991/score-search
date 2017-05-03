package client;

import java.util.List;

import org.junit.Test;

import cn.sheeva.index.Index;
import cn.sheeva.search.Searcher;
import cn.sheeva.swap.Doc;

public class Client {
    @Test
    public void run(){
        Index index=index();
        List<Doc> docs=search(index);
        for (Doc doc : docs) {
            System.out.println(doc.getText());
        }
    }
    
    private Index index(){
        Index index=new Index();
        String doc0=new String("solr es lucene zookeeper hbase mysql hive");
        String doc1=new String("solr solr");
        String doc2=new String("solr");
        String doc3=new String("solr hive");
        String doc4=new String("hive");
        String doc5=new String("solr solr solr");
        
        index.add(doc0);
        index.add(doc1);
        index.add(doc2);
        index.add(doc3);
        index.add(doc4);
        index.add(doc5);
        
        return index;
    }
    
    private List<Doc> search(Index index){
        Searcher searcher=new Searcher(index);
        List<Doc> docs=searcher.search("mysql solr");
        return docs;
    }
}
