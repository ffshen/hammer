package org.hammer.mvc;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class DefaultWebApiResult {

    public static final  String SUCCESS_CODE = "00000";
    
    public static final  String SUCCESS_MSG = "OK";
    
    public static final String BUSINESS_BUSY_FAIL_CODE = "-1";
    
    public static final String BUSINESS_BUSY_FAIL_MSG = "系统繁忙,请稍后再试";
    
    public static final String LOSE_LOGININFO_FAIL_CODE = "-2";
    
    public static final String LOSE_LOGININFO_FAIL_MSG = "登录信息丢失，请重新登录";

    private static final DefaultWebApiResult SUCCESS_WITH_NO_RESULT = new DefaultWebApiResult(SUCCESS_MSG, SUCCESS_CODE);

    private String retCode;
    private String resMsg;
    private Object result;

    public DefaultWebApiResult() {
    }

    public DefaultWebApiResult(Object result) {
        this.retCode = SUCCESS_CODE;
        this.resMsg = SUCCESS_MSG;
        this.result = result;
    }

    public DefaultWebApiResult(String resMsg, String retCode) {
        this.resMsg = resMsg;
        this.retCode = retCode;
    }

    public DefaultWebApiResult(String retCode, String resMsg, Object result) {
        this.retCode = retCode;
        this.resMsg = resMsg;
        this.result = result;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * 创建一个成功的返回, 但不需要返回空数据。
     *
     * @return
     */
    public static DefaultWebApiResult success() {
        return SUCCESS_WITH_NO_RESULT;
    }

    /**
     * 创建一个成功的返回, 并返回指定的数据。
     *
     * @return
     */
    public static DefaultWebApiResult success(Object data) {
        DefaultWebApiResult result = new DefaultWebApiResult(SUCCESS_MSG, SUCCESS_CODE);
        result.setResult(data);
        return result;
    }

    /**
     * 创建一个失败的返回。
     *
     * @return
     */
    public static DefaultWebApiResult failure(String errCode, String errMsg) {
        return new DefaultWebApiResult(errMsg, errCode);
    }

    /**
     * 创建一个失败的返回, 同时返回数据。
     *
     * @return
     */
    public static DefaultWebApiResult failure(String errCode, String errMsg, Object object) {
        return new DefaultWebApiResult(errCode, errMsg, object);
    }

}
