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
	<h1>You are going to setup ${repository}</h1>
	<form action="setup" method="post">
	<input type="hidden" name="action" value="setup"/>
	<input type="hidden" name="repoName" value="${repository}"/>
	<label>Title:</label><input type="text" name="title"/><br>
	<label>Description:</label><input type="text" name="description"/><br>
	<label>Domain:</label><input type="text" name="domain"/><br>
	<input type="submit" value="Submit"/>
	</form>
	
	</body>
</html>