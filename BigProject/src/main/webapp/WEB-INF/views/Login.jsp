<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>Login</title>
	<link rel="stylesheet" href="styles/style.css">	
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" >
</head>

<body>
	<%@ include file="Banner.jsp" %>
	<main>
		<% if (request.getAttribute("error") != null ) { %>
			<div class="alert alert-warning">
				<%= request.getAttribute("error") %>
			</div>
		<% } %>
		<form action="<%= request.getContextPath() %>/AuthenticationController?action=logIn" method="post">
			<label for="login">User</label>
			<input type="text" name="login" />
			
			<label for="password">Password</label>
			<input type="password" name="password" />
			
			<input type="submit" />
		</form>
		
	</main>
</body>
</html>