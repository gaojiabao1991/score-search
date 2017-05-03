package cn.sheeva.swap;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Doc {
    public Doc(long id, String text) {
        this.id=id;
        this.text=text;
    }
    

    private long id;
    private String text;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
