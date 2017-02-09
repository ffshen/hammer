package org.hammer.vo;

import java.io.Serializable;

/**
 * @author shenx
 * @date 2017年1月14日
 * @see
 * @version
 */
public class TestResultVO implements Serializable{
 
    private static final long serialVersionUID = -1624035710170016726L;
    
    private Integer retCode ;
    
    private String retMsg ;
    
    public TestResultVO(Integer code ,String msg){
        this.retCode = code ;
        this.retMsg = msg ;
    }

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }
    
    
    

}
