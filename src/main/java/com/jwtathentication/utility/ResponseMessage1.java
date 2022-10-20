package com.jwtathentication.utility;

import java.util.List;

/**
 * THis class is java bean class for format of response generated in any API
 * call
 * 
 * @author Ashvin
 * 
 */
public class ResponseMessage1<T> {

	private Integer status;
	private String message;
	private T data;
	private List<?> dataList;

	public ResponseMessage1(Integer status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
	public ResponseMessage1(Integer status, String message, List<?> dataList) {
		super();
		this.status = status;
		this.message = message;
		this.dataList = dataList;
	}
	public ResponseMessage1(Integer status, T data) {
		super();
		this.status = status;
		this.data = data;
	}

	public ResponseMessage1(Integer status, String message, T data, List<?> dataList) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
		this.dataList = dataList;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<?> getDataList() {
		return dataList;
	}

	public void setDataList(List<?> dataList) {
		this.dataList = dataList;
	}

}
