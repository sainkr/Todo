<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="ConnectDB.*" %>
<%@ page import="ConnectJSP.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");
	String name = request.getParameter("name");
	String host_id = request.getParameter("host_id");
	String code = request.getParameter("code");
	String add_member = request.getParameter("add_member");
	String delete_member = request.getParameter("delete_member");

	ChallengeAddCnt challengecnt = new ChallengeAddCnt(type, name, host_id, code, add_member, delete_member);
	
	String returns = challengecnt.getResult();
	
	out.clear();
	out.print(returns);
	out.flush();
%>