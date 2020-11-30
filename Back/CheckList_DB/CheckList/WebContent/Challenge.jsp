<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="ConnectDB.*" %>
<%@ page import="ConnectJSP.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");
	String name = request.getParameter("name");
	String member = request.getParameter("member");

	ChallengeCnt challengecnt = new ChallengeCnt(type, name, member);
	
	String returns = challengecnt.getResult();
	
	out.clear();
	out.print(returns);
	out.flush();
%>