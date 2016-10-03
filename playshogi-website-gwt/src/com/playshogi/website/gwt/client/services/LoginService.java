package com.playshogi.website.gwt.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	static String UNKNOWN_USERNAME = "UNKNOWN";
	static String INVALID_PASSWORD = "INVALID";
	static String LOGIN_OK = "LOGIN_OK";

	String login(String username, String password);

}
