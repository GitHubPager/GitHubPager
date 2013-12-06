<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Init Account</title>
 
<link rel="stylesheet" type="text/css" href="css/demo_1.css" />
<link rel="stylesheet" type="text/css" href="css/style_1.css" />
<link rel="stylesheet" type="text/css" href="css/button.css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.backstretch.min.js"></script>
<link href='http://fonts.googleapis.com/css?family=Josefin+Slab:400,700' rel='stylesheet' type='text/css' />
</head>
<body>
	<h1 style="font-size:60px;">Warning:</h1>
	<h1>You are going to init github page function. You must have a verifed mail.</h1>
	<form action="panel" method="post">
	<h1>
	<input type="hidden" name="action" value="commitInitAccount"/>
	<input type="submit" class="button danger" value="Okay! Go for it"/>
	</h1>
	</form>
<script type="text/javascript">
            $(function() {
				$.backstretch("images/bg2.jpg");
            });
</script>
</body>
</html>