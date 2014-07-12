<%@page import="com.google.appengine.api.users.User"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Note to Future Self</title>
</head>
<body>
	<%
		final UserService userService = UserServiceFactory.getUserService();
		final User currentUser = userService.getCurrentUser();
		final String logoutURL = userService.createLogoutURL(request.getRequestURI());
	%>
	<h1>Note to Future Self</h1>
	<p>
		Schedule emails to yourself at <%=currentUser.getEmail()%> to be received in
		the future. <a href="<%=logoutURL%>">Log out</a> to use a different account.
	</p>
</body>
</html>