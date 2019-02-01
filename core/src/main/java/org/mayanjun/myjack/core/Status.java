/*
 * Copyright 2016-2018 mayanjun.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
