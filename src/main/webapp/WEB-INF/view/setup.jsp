<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<html>
	<head>
		<meta charset="utf-8">
		<title>Welcome</title>
		<link rel="stylesheet" type="text/css" href="css/demo_1.css" />
<link rel="stylesheet" type="text/css" href="css/style_1.css" />
<link rel="stylesheet" type="text/css" href="css/button.css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.backstretch.min.js"></script>
<link href='http://fonts.googleapis.com/css?family=Josefin+Slab:400,700' rel='stylesheet' type='text/css' />
		
	</head> 
	<body>
	<div id="container">
	<h1>Welcome</h1>
	<h1>You are going to setup ${repository}</h1>
	<form action="panel?action=commitSetup" method="post">
	<input type="hidden" name="action" value="setup"/>
	<input type="hidden" name="repositoryName" value="${repository}"/>
	<h1><label>Title:</label><input type="text" class="button" name="title"/></h1>
	<h1><label>Description:</label><input type="text" class="button" name="description"/><br></h1>
	<h1><label>Theme:</label><select name="template" class="button">
	<c:forEach var="template" items="${templateList}">
		<option value="${template.getUrl()}" />${template.getName()}</option>
	</c:forEach>
	</select></h1>
	<h1><input type="submit" class="button" value="Submit"/></h1>
	
	
	</form>
	</div>
	<script type="text/javascript">
            $(function() {
				$.backstretch("images/bg2.jpg");
            });
	</script>
	</body>
</html>