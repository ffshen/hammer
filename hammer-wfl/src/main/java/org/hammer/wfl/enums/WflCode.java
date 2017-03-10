package org.hammer.wfl.enums;

/**
 * @author shenx
 * @date 2016年12月13日
 * @see
 * @version
 */
public enum WflCode {

    AUTO_CANCEL_ORDER("自动取消订单", "AUTO_CANCEL_ORDER"),

    FORK_JOIN_TEST("并发测试流程", "FORK_JOIN_TEST"),    

    OFC_CANCEL_ORDER("OFC回推取消订单流程", "OFC_CANCEL_ORDER"),
    
    OFC_RECEIVED_ORDER("OFC回推收货流程", "OFC_RECEIVED_ORDER"),
    
    ;

    private String name;
    private String value;

    private WflCode(String name, String value) {
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
