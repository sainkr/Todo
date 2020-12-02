<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="ConnectDB.*" %>
<%@ page import="ConnectJSP.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");
	String name = request.getParameter("name");
	String host_id = request.getParameter("host_id");
	String member = request.getParameter("member");

	ChallengeCnt challengecnt = new ChallengeCnt(type, name, host_id, member);
	
	String returns = challengecnt.getResult();
	
	out.clear();
	out.print(returns);
	out.flush();
%>