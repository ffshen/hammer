package org.hammer.wfl.enums;

/**
 * @author shenx
 * @date 2016年12月13日
 * @see
 * @version
 */
public enum IStepStatus {
    
    INIT("未执行", "0"),
    
    FINISH("已执行", "1") ,

    DISPATCH("已调度", "6") ,
    
    ;

    private String name;
    private String value;

    private IStepStatus(String name, String value) {
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
