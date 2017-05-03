package cn.sheeva.index;

public class DocIdGenerator {
    private static long cur=-1;
    
    protected static synchronized long generate(){
        cur++;
        return cur;
    }
}
