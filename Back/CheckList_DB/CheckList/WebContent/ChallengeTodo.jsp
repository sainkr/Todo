<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="ConnectDB.*" %>
<%@ page import="ConnectJSP.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");
	String my_id = request.getParameter("my_id");
	String code = request.getParameter("code");
	String num = request.getParameter("num");

	ChallengeTodoCnt challengecnt = new ChallengeTodoCnt(type, my_id, code, num);
	
	String returns = challengecnt.getResult();
	
	out.clear();
	out.print(returns);
	out.flush();
%>