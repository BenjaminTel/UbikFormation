<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"	
    import="com.ubik.formation.entities.Auteur"
    import="java.util.ArrayList"
  
    %>
     <%--  isErrorPage="false"
	errorPage="/ErrorHandler.jsp" --%>
    
<% 	
	ArrayList<Auteur> auteurs = (ArrayList<Auteur>)request.getAttribute("auteurs");
	int current_page = (int)request.getAttribute("page");
	int totalPages = (int)request.getAttribute("totalPages");
	String status = (String)request.getAttribute("status"); 

%>
<!DOCTYPE html>
<html>
<head>
	<title>Authors</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" >	
</head>
<body>
	<%@ include file="Banner.jsp" %>

	<main>
		<% if (auteurs != null) { %>
			<% if (status != null && "updated".equals(status)) { %>
				<div class="alert alert-success">
					Author updated with success.
				</div>
			<% } else if (status != null && "created".equals(status)) { %>
				<div class="alert alert-success">
					Author created with success.
				</div>
			<% } %>
		<table class="table table-striped">
			<thead>
				<tr>
					<th><a href="<%= request.getContextPath() %>/AuteurController?action=form"> + Add new author </a></th>			
					<th scope="col">ID</th>
					<th scope="col">First name</th>
					<th scope="col">Last name</th>		
				</tr>
			</thead>
			<tbody>
			<% for (Auteur auteur : auteurs) { %>
				<tr>
					<td><a href ="<%= request.getContextPath() %>/AuteurController?action=delete&auteurId=<%=auteur.getId()%>">Delete</a></td>				
					<td><a href="<%= request.getContextPath() %>/AuteurController?action=form&auteurId=<%=auteur.getId() %>"><%=auteur.getId() %></a></td>
					<td><a href="<%= request.getContextPath() %>/AuteurController?action=form&auteurId=<%=auteur.getId() %>"><%=auteur.getPrenom() %></a></td>
					<td><a href="<%= request.getContextPath() %>/AuteurController?action=form&auteurId=<%=auteur.getId() %>"><%=auteur.getNom() %></a></td>
				</tr>
			<% } %>
			</tbody>
		</table>
		<div>
		    <!-- Pagination -->
		    <% if (Integer.valueOf(current_page) > 1) { %>
		        <a href="<%= request.getContextPath() %>/AuteurController?action=list&page=<%= current_page - 1 %>">Previous</a>
		    <% } %>
		    
		    <% for (int i = 1; i <= totalPages; i++) { %>
		    	<% if (current_page != i) { %>
		        	<a href="<%= request.getContextPath() %>/AuteurController?action=list&page=<%= i %>"><%= i %></a>
		        <% } %>
		    <% } %>
		    
		    <% if (current_page < totalPages) { %>
		        <a href="<%= request.getContextPath() %>/AuteurController?action=list&page=<%= current_page +1 %>">Next</a>
		    <% } %>
		</div>
		<% } %>
	</main>
</body>
</html>