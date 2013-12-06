<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="panel?action=commitEditSetting" method="post">
<label>Title</label><input type="text" name="title" value="${setting.getTitle()} "/>
<label>Description</label><input type="text" name="description" value="${setting.getDescription()} "/>
<label>Domain</label><input type="text" name="domain" value="${setting.getDomain()} "/>
<label>Article Per Page</label><input type="text" name="articlePerPage" value="${setting.getArticlePerPage()} "/>
<label>DiqusId</label><input type="text" name="diqusId" value="${setting.getDiqusId()} "/>
<label>Footer</label><input type="text" name="footer" value="${setting.getFooter()} "/>
<input type="submit" value="Finish"/>
</form>
</body>
</html>
