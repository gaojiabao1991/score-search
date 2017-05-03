package cn.sheeva.util;

public class Formula {
    public static double tf_idf(long tf,long docNum, long df){
        return tf*Math.log((double)docNum/(double)df);
    }
}
