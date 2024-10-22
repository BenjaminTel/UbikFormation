<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"	
    import="com.ubik.formation.entities.Livre"
    import="java.util.ArrayList"

    %>
    
<% 	
	ArrayList<Livre> livres = (ArrayList<Livre>)request.getAttribute("livres");
	int current_page = (int)request.getAttribute("page");
	int totalPages = (int)request.getAttribute("totalPages");
	String status = (String)request.getAttribute("status"); 

%>
<!DOCTYPE html>
<html>
<head>
	<title>Books</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" >	
</head>
<body>
	<%@ include file="Banner.jsp" %>
	<main>
		<% if (livres != null) { %>
			<% if (status != null && "updated".equals(status)) { %>
				<div class="alert alert-success">
					Book updated with success.
				</div>
			<% } else if (status != null && "created".equals(status)) { %>
				<div class="alert alert-success">
					Book created with success.
				</div>
			<% } %>
		<table class="table table-striped">
			<thead>
				<tr>
					<th><a href="<%= request.getContextPath() %>/LivreController?action=form"> + Add new book</a></th>			
					<th scope="col">ID</th>
					<th scope="col">Author</th>
					<th scope="col">Title</th>
					<th scope="col">Publication date 
						<a href="<%= request.getContextPath() %>/LivreController?action=list&page=<%=current_page %>&sortBy=date&order=asc"> ↑ </a>
						<a href="<%= request.getContextPath() %>/LivreController?action=list&page=<%=current_page %>&sortBy=date&order=desc"> ↓ </a>
					</th>	
				</tr>
			</thead>
			<tbody>
			<% for (Livre livre: livres) { %>
				<tr>
					<td><a href ="<%= request.getContextPath() %>/LivreController?action=delete&livreId=<%=livre.getId()%>">Delete</a></td>
					<td><a href="<%= request.getContextPath() %>/LivreController?action=form&livreId=<%=livre.getId() %>"><%=livre.getId() %></a></td>
					<td><a href="<%= request.getContextPath() %>/LivreController?action=form&livreId=<%=livre.getId() %>"><%=livre.getAuteur().getPrenom() %> <%=livre.getAuteur().getNom() %></a></td>
					<td><a href="<%= request.getContextPath() %>/LivreController?action=form&livreId=<%=livre.getId() %>"><%=livre.getTitre() %></a></td>
					<td><a href="<%= request.getContextPath() %>/LivreController?action=form&livreId=<%=livre.getId() %>"><%=livre.getDateDeParution() %></a></td>
				</tr>
			<% } %>
			</tbody>
		</table>
		
		<div>
		    <!-- Pagination -->
		    <% if (Integer.valueOf(current_page) > 1) { %>
		        <a href="<%= request.getContextPath() %>/LivreController?action=list&page=<%= current_page - 1 %>">Previous</a>
		    <% } %>
		    
		    <% for (int i = 1; i <= totalPages; i++) { %>
		        <% if (current_page != i) { %>
		        	<a href="<%= request.getContextPath() %>/LivreController?action=list&page=<%= i %>"><%= i %></a>
		    	<% } %>
		    <% } %>
		    <% if (current_page < totalPages) { %>
		        <a href="<%= request.getContextPath() %>/LivreController?action=list&page=<%= current_page +1 %>">Next</a>
		    <% } %>
		</div>
		<% } %>
	</main>
</body>
</html>