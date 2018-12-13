package com.cchat.models;

public class Message {

	String message;
	String code;
	String exception;
	boolean flag;

	public Message() {
	}

	public Message(String message, String code, String exception) {
		super();
		this.message = message;
		this.code = code;
		this.exception = exception;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}
