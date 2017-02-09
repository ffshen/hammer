package org.hammer.vo;

import java.io.Serializable;

public class OperatorVo implements Serializable{
 
    private static final long serialVersionUID = -4740258789806138540L;
    
    private Integer operator;
    
    private String operaterName;

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public String getOperaterName() {
        return operaterName;
    }

    public void setOperaterName(String operaterName) {
        this.operaterName = operaterName;
    }

}
