package org.hammer.mvc.context;

import org.hammer.vo.OperatorVo;

/**
 * 
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
public class LoginContextHolder {

    public static ThreadLocal<OperatorVo> threadLocal = new ThreadLocal<OperatorVo>();
    
    public static void setOperator(OperatorVo operator) {
        threadLocal.set(operator);
    }
    
    public static OperatorVo getOperator() {
        return threadLocal.get();
    }
}
