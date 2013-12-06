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
	<h1>Repository:${repository}</h1>
	<h1><a href="panel?action=addPost&repositoryName=${repository}">Add Post</a></h1>
	<h1><a href="panel?action=logout">Logout</a></h1>
	<c:forEach var="entry" items="${entrys}">
		<h2><a href="panel?action=editPost&repositoryName=${repository}&entryId=${entry.getId()}">${entry.getTitle()}</a><a href="panel?action=deletePost&repositoryName=${repository}&entryId=${entry.getId()}">Delete</a></h2>
	</c:forEach>
	</body>
</html>
