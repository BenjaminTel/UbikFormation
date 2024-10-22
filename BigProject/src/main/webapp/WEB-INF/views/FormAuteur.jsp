<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import="com.ubik.formation.entities.Auteur"	

%>
<%	Auteur auteur = (Auteur)request.getAttribute("auteur");
	String action = auteur != null ? "update" : "add";
	%>
		
<!DOCTYPE html>
<html>
<head>
	<title>Author</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" >	
</head>
<body>
	<%@ include file="Banner.jsp" %>
	
	<main>
		
		<form action="<%= request.getContextPath() %>/AuteurController?action=<%=action %>" method="post">
			<div class="form-group">
				<label for="firstName">First name</label>
				<input type="text" class="form-control" name="firstName" value="<%=  auteur != null ? auteur.getPrenom() : ""%>"/>
			</div>
			<div class="form-group">
			<label for="lastName">Last name</label>
			<input type="text" class="form-control" name="lastName" value="<%= auteur != null ? auteur.getNom(): "" %>"/>
			</div>
			
			<input type="hidden" name="auteurId" value="<%= auteur != null ? auteur.getId() : 0 %>"/>
			<button type="submit" class="btn btn-primary">Send</button>
		</form>
		
		<div>
			<a href="<%= request.getContextPath() %>/AuteurController?action=list">Back</a>
		</div>
	</main>
</body>
</html>