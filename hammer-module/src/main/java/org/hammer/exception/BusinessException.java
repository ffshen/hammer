package org.hammer.exception;

import java.io.Serializable;

public class BusinessException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;
    /** 错误代码 */
	private String retCode;
	/** 错误消息 */
	private String resMsg;
	
	public BusinessException(String resMsg) {
		super(resMsg);
	}

	public BusinessException(){
		super();
	}
	
	public BusinessException(String retCode, String resMsg){
		this.setRetCode(retCode);
		this.setResMsg(resMsg);
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
	
	
	
}
