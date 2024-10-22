<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import="com.ubik.formation.entities.Livre"	
	import="com.ubik.formation.entities.Auteur"	
	import="com.ubik.formation.entities.Tag"	
	import="java.util.ArrayList"
	import="java.util.Set"
	import="java.util.HashSet"
%>

<%	Livre livre = (Livre)request.getAttribute("livre");
	ArrayList<Auteur> auteurs = (ArrayList<Auteur>)request.getAttribute("auteurs");
	String action = livre != null ? "update" : "add";

	String tags = "";
	String separator = ",";
	Set<String> tagList = (HashSet<String>)request.getAttribute("tags");
	if (tagList != null && tagList.size() > 0) {
		for (String tag : tagList) {
			tags += tag + separator;
		}
	}
	%>
	
		
<!DOCTYPE html>
<html>
<head>
	<title>Book</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" >	
</head>
<body>
	<%@ include file="Banner.jsp" %>
	
	<main>
		<form action="<%= request.getContextPath() %>/LivreController?action=<%=action %>" method="post">
			<div class="form-group">
				<label for="auteurId">Author</label>
				<select class="form-control" name="auteurId">
					<% for(Auteur auteur: auteurs) { %>
					<option value="<%= auteur.getId() %>"><%= auteur.getPrenom() %> <%= auteur.getNom() %></option>
					<% } %>
				</select>
			</div>
			<div class="form-group">
				<label for="titre">Title</label>
				<input type="text" class="form-control" name="titre" value="<%= livre != null ? livre.getTitre(): "" %>"/>
			</div>
			<div class="form-group">
				<label for="dateDeParution">Publication date</label>
				<input type="datetime-local" class="form-control" name="dateDeParution" value="<%= livre != null ? livre.getDateDeParution(): "" %>"/>
			</div>
			<div class="form-group">
				<label for="tags">Tags (separate by comma)</label>
				<input type="text" class="form-control" name="tags" value="<%= tags %>" placeholder="tag1,tag2,tag3,etc"/>
			</div>
			<input type="hidden" name="livreId" value="<%= livre != null ? livre.getId() : 0 %>"/>
			<button type="submit" class="btn btn-primary">Send</button>
		</form>
		
		<div>
			<a href="<%= request.getContextPath() %>/LivreController?action=list">Back</a>
		</div>
	</main>
</body>
</html>