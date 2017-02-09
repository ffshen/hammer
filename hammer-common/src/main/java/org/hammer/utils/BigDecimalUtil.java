package org.hammer.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author shenx
 * @date 2016年8月9日
 * @see
 * @version
 */
public class BigDecimalUtil {
    
    /**金额的默认scale*/
    public static final int DEFAULT_SCALE_FOR_MONEY = 2;    

    /**折扣的默认scale*/
    public static final int DEFAULT_SCALE_FOR_DISCOUNT = 2;
    
    /**折扣的默认scale*/
    public static final int DEFAULT_SCALE_FOR_DISCOUNT_INT = 1;
    
    /**金额：0*/
    public static final BigDecimal MONEY_ZERO = new BigDecimal(0.00);
    

    /**折扣：0*/
    public static final BigDecimal MONEY_DISCOUNT = new BigDecimal(0.00);
    
    
    /**一百*/
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);    
    
    /**十*/
    public static final BigDecimal TEN = new BigDecimal(10);  
    
    /**一*/
    public static final BigDecimal ONE = new BigDecimal(1);  

    public static final String  MONEY_ZERO_STR = "0.00";
    
    static {
        MONEY_ZERO.setScale(DEFAULT_SCALE_FOR_MONEY);
        MONEY_DISCOUNT.setScale(DEFAULT_SCALE_FOR_DISCOUNT)  ;
    }
    
    private BigDecimalUtil(){}

    /**
     * 配置Money。
     * 
     * @param money
     * @return
     */
    public static BigDecimal configMoney(BigDecimal money){
        if(money == null){
            money = MONEY_ZERO;
        }
        money = money.setScale(DEFAULT_SCALE_FOR_MONEY);
        return money;
    }
    
    /**
     * 转换成字符串。
     * 
     * @param money
     * @return
     */
    public static String getMoney(BigDecimal money){
        if(money == null){
            money = MONEY_ZERO;
        } 
        money = money.setScale(DEFAULT_SCALE_FOR_MONEY,RoundingMode.HALF_UP);
        return new java.text.DecimalFormat("#0.00").format(money);
    }
    
    
    
    /**
     * 判断是否整数
     * @return
     */
    public static boolean isInteger(BigDecimal decimal){
        BigDecimal decimal_int = decimal.setScale(1, RoundingMode.DOWN) ;
        return decimal_int.compareTo(decimal)==0?true:false ;        
    }
    
    public static String getDiscount(BigDecimal discount){
        if(discount == null){
            discount = MONEY_DISCOUNT;
        }
        if(discount.compareTo(ONE)==0){
            return "" ;
        }
        if(!isInteger(discount)){
            discount = discount.multiply(TEN).setScale(DEFAULT_SCALE_FOR_DISCOUNT,RoundingMode.DOWN); 
            return new java.text.DecimalFormat("#0.00").format(discount);            
        }
        else{
            discount = discount.multiply(TEN).setScale(DEFAULT_SCALE_FOR_DISCOUNT_INT,RoundingMode.DOWN);  
            return new java.text.DecimalFormat("#0.0").format(discount);             
        }
    }
    
    /**
     * 将小数转换成百分比表示，比如0。01转换成1%，一般用于显示。
     * 
     * @param decimal
     * @return
     */
    public static BigDecimal getPercent(BigDecimal decimal){
        return decimal.multiply(ONE_HUNDRED);
    }
    
    /**
     * 将百分比转成小数表示形式，比如1%转换成0.01,一般用于计算。
     * 
     * @param percent
     * @return
     */
    public static BigDecimal getDecimal(BigDecimal percent){
        return percent.divide(ONE_HUNDRED);
    }
}
