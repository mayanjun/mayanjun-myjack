package org.mayanjun.myjack.core;

import java.util.Map;

/**
 * This class represent a exception of service. This exception should be caught
 * by business code. Note that this class must hold a correct status that to be
 * returned to client
 * @author mayanjun
 * @since 1.0
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -4983415461842048680L;
	private Status status;
	private Map<String, Object> holder;
	private String log;

	public ServiceException(String message) {
		this(Status.INTERNAL_ERROR, message, null);
	}
	
	public ServiceException(String message, String log) {
		this(Status.INTERNAL_ERROR, message, log);
	}

	public ServiceException(Status code) {
		this(code, null, null);
	}

    public ServiceException(Status code, String log, Object data) {
        this(code, null, log);
        this.data = data;
    }

	public ServiceException(Status code, String log, Object data, Throwable cause) {
		this(code, null, log, cause);
		this.data = data;
	}
	
	public ServiceException(Status code, String message) {
		this(code, message, null);
	}

	private Object data;

	public ServiceException(Status code, String message, String log) {
		super(isEmpty(message) ? (code == null ? Status.INTERNAL_ERROR.getMessage() : code.getMessage()) : message);
		if (code == null) code = Status.INTERNAL_ERROR;
		this.status = new Status(code.getCode(), this.getMessage());
		this.log = log;
	}

	public ServiceException(Status code, String message, String log, Throwable cause) {
		super(isEmpty(message) ? (code == null ? Status.INTERNAL_ERROR.getMessage() : code.getMessage()) : message, cause);
		if (code == null) code = Status.INTERNAL_ERROR;
		this.status = new Status(code.getCode(), this.getMessage());
		this.log = log;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Map<String, Object> getHolder() {
		return holder;
	}

	public void setHolder(Map<String, Object> holder) {
		this.holder = holder;
	}

	public String getLog() {
		return log;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return super.fillInStackTrace();
	}

	private static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
