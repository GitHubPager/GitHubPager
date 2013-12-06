 <!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<html lang="en">
    <head>
        <title>GitHubPager!</title>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <meta name="author" content="Codrops" />
        <link rel="shortcut icon" href="../favicon.ico"> 
        <link rel="stylesheet" type="text/css" href="css/demo_1.css" />
        <link rel="stylesheet" type="text/css" href="css/style_1.css" />
        <link rel="stylesheet" type="text/css" href="css/button.css" />
        <link href='http://fonts.googleapis.com/css?family=Josefin+Slab:400,700' rel='stylesheet' type='text/css' />
		<noscript>
			<style>
				.st-accordion ul li{
					height:auto;
				}
				.st-accordion ul li > a span{
					visibility:hidden;
				}
			</style>
		</noscript>
    </head>
    <body>
        <div class="container">
            
            <h1>Repository:<span> ${repository}</span></h1>
			<div class="more">
				<a href="panel?action=addPost&repositoryName=${repository}" >Add Post</a>
				<a href="panel?action=editSetting&repositoryName=${repository}">Setting</a>
				<p><strong>In this panel:</strong> You can manage all your entries!</p>
			</div>
            <div class="wrapper">
                <div id="st-accordion" class="st-accordion">
                    <ul>
                        
						<c:forEach var="entry" items="${entrys}">
						<li>
							<a href="panel?action=editPost&repositoryName=${repository}&entryId=${entry.getId()}">${entry.getTitle()}<span class="st-arrow">Open or Close</span></a>
						    <div class="st-content">
                            <p>${entry.getTitle()}</p>
                            <a href="panel?action=editPost&repositoryName=${repository}&entryId=${entry.getId()}" class="button pill">Edit</a>
                            <a href="panel?action=deletePost&repositoryName=${repository}&entryId=${entry.getId()}" class="button pill">Delete</a>
                            </div>
						</li>
						</c:forEach>                     
                    </ul>
                </div>
            </div>
            <div class="more">
            	<c:forEach var="x" begin="1" end="${totalPage}"  step="1">
            	<a href="panel?action=addPost&repositoryName=${repository}&page=${x}" <c:if test="${x==currentPage}"> class="current" </c:if> >${x}</a>
            	</c:forEach>
			</div>
        </div>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
		<script type="text/javascript" src="js/jquery.accordion.js"></script>
		<script type="text/javascript" src="js/jquery.easing.1.3.js"></script>
		<script type="text/javascript" src="js/jquery.backstretch.min.js"></script>
        <script type="text/javascript">
            $(function() {
			
				$('#st-accordion').accordion();
				$.backstretch("images/bg1.jpg");
				
            });
        </script>
    </body>
</html>	



