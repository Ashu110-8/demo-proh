package com.springrest.fnt.springrestfnt.service;

public class ResponseStatus {
	int errorCode;
	String subErrorCode;
    String message;
    Boolean success;
    public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getSubErrorCode() {
		return subErrorCode;
	}
	public void setSubErrorCode(String subErrorCode) {
		this.subErrorCode = subErrorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	@Override
	public String toString() {
		return "ResponseStatus [errorCode=" + errorCode + ", subErrorCode=" + subErrorCode + ", message=" + message
				+ ", success=" + success + ", getErrorCode()=" + getErrorCode() + ", getSubErrorCode()="
				+ getSubErrorCode() + ", getMessage()=" + getMessage() + ", getSuccess()=" + getSuccess()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
}
