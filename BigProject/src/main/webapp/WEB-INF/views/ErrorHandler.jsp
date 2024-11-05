<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isErrorPage="True"%>
<!DOCTYPE html>
<html>
<head>
	<title>Error</title>	
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" >	
</head>
<body>
	<main>
		<%@ include file="Banner.jsp" %>
		<h1>Une erreur est survenue</h1>
	    <div class="alert alert-danger" role="alert">
	    	<p>${errorMessage}</p>
    	</div>
	</main>
</body>
</html>