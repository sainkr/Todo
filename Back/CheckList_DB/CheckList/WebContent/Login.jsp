<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="ConnectDB.*" %>
<%@ page import="ConnectJSP.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");
	String id = request.getParameter("id");
	String password = request.getParameter("password");
	String name = request.getParameter("name");
	
	LoginCnt login = new LoginCnt(type,id,password,name);
	
	String returns = login.getResult();
	
	out.clear();
	out.print(returns);
	out.flush();
%>