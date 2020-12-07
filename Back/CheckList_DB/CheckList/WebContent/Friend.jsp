<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="ConnectDB.*" %>
<%@ page import="ConnectJSP.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");
	String my_id = request.getParameter("my_id");
	String friend_id = request.getParameter("friend_id");

	FriendCnt requestcnt = new FriendCnt(type, my_id, friend_id);
	
	String returns = requestcnt.getResult();
	
	out.clear();
	out.print(returns);
	out.flush();
%>