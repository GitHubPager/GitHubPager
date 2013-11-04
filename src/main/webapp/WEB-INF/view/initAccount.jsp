<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Init Account</title>
</head>
<body>
	<h1>User: ${userInfo.getLogin()}</h1>
	<h1>You are going to init github page function. You must have a verifed mail.</h1>
	<form action="panel" method="post">
	<input type="hidden" name="action" value="commitInitAccount"/>
	<input type="submit" value="Init"/>
	</form>
</body>
</html>