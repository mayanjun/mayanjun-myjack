package org.mayanjun.myjack.core;


import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * @author mayanjun
 * @since 0.0.1
 */
public class Status implements Serializable {
	private static final long serialVersionUID = -2014592760065018707L;

	public static final Status OK 					= new Status(0, "OK");
	
	public static final Status INTERNAL_ERROR 		= new Status(1000, "服务器内部错误");
	public static final Status PARAM_MISS			= new Status(1001,"缺少参数");
	public static final Status PARAM_ERROR 			= new Status(1002,"参数错误");


	public Status() {
	}

	public Status(int code, String msg) {
		this.code = code;
		this.message = msg;
	}

	@XmlElement
	private int code;

	@XmlElement
	private String message;

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}


	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
