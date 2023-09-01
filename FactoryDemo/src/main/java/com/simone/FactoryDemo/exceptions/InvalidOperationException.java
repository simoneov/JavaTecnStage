package com.simone.FactoryDemo.exceptions;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class InvalidOperationException extends Exception{

	private String msg;
	private HttpStatusCode statusCode;
	
	public InvalidOperationException(String msg, HttpStatusCode statusCode) 
	{
		super(msg);
		this.msg = msg;
		this.statusCode = statusCode;
	}
	
	public InvalidOperationException(String msg) 
	{
		super(msg);
		this.msg = msg;
	}

}
