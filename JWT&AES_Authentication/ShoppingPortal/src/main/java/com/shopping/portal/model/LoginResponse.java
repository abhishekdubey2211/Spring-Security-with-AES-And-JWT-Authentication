package com.shopping.portal.model;

import java.util.Date;
import java.util.List;

public class LoginResponse {
	private Date responseDatetime;
	private String JWT_Token;
	private String userName;
	private List<String> roles;

	public Date getResponseDatetime() {
		return responseDatetime = new Date();
	}

	public String getJWT_Token() {
		return JWT_Token;
	}

	public void setJWT_Token(String jWT_Token) {
		JWT_Token = jWT_Token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public LoginResponse(Date responseDatetime, String jWT_Token, String userName, List<String> roles) {
		super();
		this.responseDatetime = responseDatetime;
		JWT_Token = jWT_Token;
		this.userName = userName;
		this.roles = roles;
	}

	public LoginResponse() {
		super();
	}

}
