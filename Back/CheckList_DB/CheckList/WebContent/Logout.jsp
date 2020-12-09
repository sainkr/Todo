<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="ConnectDB.*" %>
<%@ page import="ConnectJSP.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");
	String id = request.getParameter("id");
	String date= request.getParameter("date");
	String content = request.getParameter("content");
	String check = request.getParameter("check");
	String weather = request.getParameter("weather");
	
	LogoutCnt logout = new LogoutCnt(type, id,date, content, check, weather);
	
	String returns = logout.getResult();
	
	out.clear();
	out.print(returns);
	out.flush();
%>