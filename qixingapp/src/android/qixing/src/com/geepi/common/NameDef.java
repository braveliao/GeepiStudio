package com.geepi.common;


public interface NameDef {
	public final static String kResponseStatus = "status";
	public final static String kUserName = "userid";
	public final static String kPassWord = "password";
	public final static String kResponseResult = "item";

	//method 
	public final static String kValMethodLogin = "/auth/login.json";


    
	//network
	public final static int kTimeOut = -1;
	public final static int kRequestError = -2;
	public final static int kTokenInvalid = -3;

}
