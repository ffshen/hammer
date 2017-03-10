package org.hammer.wfl.enums;

/**
 * @author shenx
 * @date 2016年12月13日
 * @see
 * @version
 */
public enum RStepType {
    START("开始节点", "S"),
    
    NORMAL("普通节点", "N") ,
    
    FORK("分支节点", "FO") ,
    
    JOIN("合并节点", "JO") ,
    
    CONDITION("条件节点", "CO") ,
    
    EXCEPTION("异常节点", "EX") ,

    END("结束节点", "E") ,
    ;

    private String name;
    private String value;

    private RStepType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
