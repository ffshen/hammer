package org.hammer.wfl.enums;

/**
 * @author shenx
 * @date 2016年12月13日
 * @see
 * @version
 */
public enum RStepCode {
    START("开始", "START"),
    RELEASE_STOCK("回滚库存", "RELEASE_STOCK"),
    RELEASE_COUPON("回滚优惠券", "RELEASE_COUPON"),
    RELEASE_DOCPOINT("回滚积分", "RELEASE_DOCPOINT"),   
    RELEASE_FIVEDISCOUNT("回滚首五", "RELEASE_FIVEDISCOUNT"),   
    RELEASE_SUNLIGHT("回滚阳光化", "RELEASE_SUNLIGHT"),  
    REC_FIVEDISCOUNT("收货-首五", "REC_FIVEDISCOUNT"),  
    REC_USERPOINT("收货-用户积分", "REC_USERPOINT"),   
    REC_DOCPOINT("收货-解冻医生积分", "REC_DOCPOINT"), 
    END("完成", "END") ,
    
    ;

    private String name;
    private String value;

    private RStepCode(String name, String value) {
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
