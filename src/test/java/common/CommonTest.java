package common;

import java.util.Collections;
import java.util.PriorityQueue;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

public class CommonTest {
    @Test
    public void test(){
        PriorityQueue<Integer> q=new PriorityQueue<>(Collections.reverseOrder());
        q.add(8);
        q.add(3);
        q.add(3);
        q.add(9);
        q.add(3);
        q.add(1);
        q.add(2);
        
        
        Integer i=null;
        while ((i=q.poll())!=null) {	
            System.out.println(i);
        }
    }
    
    @Test
    public void test1(){
        Stu stu=new Stu("gaojiabao", 26);
        System.out.println(stu);
    }
    
    private static class Stu{
        public Stu(String name,int age) {
            this.name=name;
            this.age=age;
        }
        
        private String name;
        private int age;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}
