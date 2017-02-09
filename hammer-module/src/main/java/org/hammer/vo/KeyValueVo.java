package org.hammer.vo;

import java.io.Serializable;

/**
 * @author shenx
 * @date 2016年8月8日
 * @see
 * @version
 */
public class KeyValueVo  implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -1332280598423976324L;
    
    public KeyValueVo(){
        super() ;
    }
    
    public KeyValueVo(Integer key , String value){
        super() ;
        this.key = key ;
        this.value = value ;
    }
    
    private Integer key ;
    
    private String value ;

    

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    

}
