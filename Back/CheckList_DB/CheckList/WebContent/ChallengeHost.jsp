<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="ConnectDB.*" %>
<%@ page import="ConnectJSP.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");
	String code = request.getParameter("code");
	String num = request.getParameter("num");
	String content = request.getParameter("content");
	ChallengeHostCnt challengecnt = new ChallengeHostCnt(type, code, num, content);
	
	String returns = challengecnt.getResult();
	
	out.clear();
	out.print(returns);
	out.flush();
%>