<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="ConnectDB.*" %>
<%@ page import="ConnectJSP.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String my_id = request.getParameter("my_id");
	String frined_id = request.getParameter("frined_id");

	RequestCnt requestcnt = new RequestCnt(my_id, frined_id);
	
	String returns = requestcnt.getResult();
	
	out.clear();
	out.print(returns);
	out.flush();
%>