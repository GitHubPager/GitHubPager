<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<html>
<head>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="http://tinymce.cachefly.net/4.0/tinymce.min.js"></script>
<script type="text/javascript" src="https://www.dropbox.com/static/api/1/dropins.js" id="dropboxjs" data-app-key="bf1mlms18dgzf6g"></script>
<script>
$(function() {
    $( "input[type=submit], a, button" )
      .button();
});
</script>
<script>
        tinymce.init({selector:'textarea',
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
<h1>User: ${userInfo.getLogin()}</h1>
	<h1>Repository:${repository}</h1>
<form action="panel?action=commitEditPost" method="post">
<label for="title">Title:</label><input type="text" name="title" value="${entry.getTitle()}" >
<textarea name="content">${entry.getContent()}</textarea>
<input type="dropbox-chooser" name="selected-file"  id="db-chooser"/>
<input type="hidden" name="repositoryName" value="${repository}"/>
<input type="hidden" name="entryId" value="${entry.getId()}"/>
<input type="submit" value="Finish"/>
</form>
<script type="text/javascript">
    // add an event listener to a Chooser button
    document.getElementById("db-chooser").addEventListener("DbxChooserSuccess",
        function(e) {
			var dlLink=e.files[0].link.replace("https://www","https://dl");
            alert("Here's the chosen file: " + dlLink);
        }, false);
</script>

</body>
</html>