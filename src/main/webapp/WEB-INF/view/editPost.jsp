<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<html>
<head>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://tinymce.cachefly.net/4.0/tinymce.min.js"></script><script type="text/javascript" src="js/jquery.backstretch.min.js"></script>
<script type="text/javascript" src="https://www.dropbox.com/static/api/1/dropins.js" id="dropboxjs" data-app-key="bf1mlms18dgzf6g"></script>
<link rel="stylesheet" type="text/css" href="css/demo_1.css" />
<link rel="stylesheet" type="text/css" href="css/style_1.css" />
<link rel="stylesheet" type="text/css" href="css/button.css" />
 <link href='http://fonts.googleapis.com/css?family=Josefin+Slab:400,700' rel='stylesheet' type='text/css' />
<script>
        tinymce.init({selector:'textarea',
        height:600,
		plugins: [
        "advlist autolink lists link image charmap print preview anchor",
        "searchreplace visualblocks code fullscreen",
        "insertdatetime media table contextmenu paste"
		],
		toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image media"
		});
</script>
</head>
<body>
<div class="container" style="width:60%;margin:auto;">
<h1>Edit Post</h1>
<form action="panel?action=commitEditPost" method="post">
<label for="title" class="button danger">Title:</label><input class="button big" type="text" name="title" style="width:80%" value="${entry.getTitle()}"><br><br>
<textarea name="content">
<c:out value="${entry.getContent()}" escapeXml="true"/></textarea>
<input type="dropbox-chooser" name="selected-file"  id="db-chooser"/>
<input type="hidden" name="repositoryName" value="${repository}"/>
<input type="hidden" name="entryId" value="${entry.getId()}"/>
<input type="submit" class="button pill" value="Submit"/>
</form>
</div>
<script type="text/javascript">
    // add an event listener to a Chooser button
    document.getElementById("db-chooser").addEventListener("DbxChooserSuccess",
        function(e) {
			var dlLink=e.files[0].link.replace("https://www","https://dl");
            alert("Here's the chosen file: " + dlLink);
        }, false);
</script>
<script type="text/javascript" src="js/jquery.backstretch.min.js"></script>
<script type="text/javascript">
            $(function() {
				$.backstretch("images/bg1.jpg");
            });
</script>
</body>
</html>
