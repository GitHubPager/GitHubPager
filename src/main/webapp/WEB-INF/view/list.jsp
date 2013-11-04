<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<html>
	<head>
		<meta charset="utf-8">
		<title>Welcome</title>
	</head> 
	<body>
	<h1>User: ${userInfo.getLogin()}</h1>
	<h1><a href="panel?action=logout">Logout</a></h1>
	<c:forEach var="message" items="${repository}">
		<h2><a href="panel?action=manageRepository&repositoryName=${message.getName()}">${message.getName()}</a></h2>
	</c:forEach>
	</body>
</html>
