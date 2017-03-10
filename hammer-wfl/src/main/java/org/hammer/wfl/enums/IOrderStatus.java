package org.hammer.wfl.enums;

/**
 * @author shenx
 * @date 2016年12月12日
 * @see
 * @version
 */
public enum IOrderStatus {

    EXE("执行中", "0"),
    
    END("完成", "1") ,
    
    EXCEPTION("异常", "2") ,
    
    ;

    private String name;
    private String value;

    private IOrderStatus(String name, String value) {
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
