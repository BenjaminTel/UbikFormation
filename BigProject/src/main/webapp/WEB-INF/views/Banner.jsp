<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"	
    import="com.ubik.formation.entities.Utilisateur"

    %>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js"></script>
<header>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<div>
			<span class="text-white nav-link">Big Project</span>
		</div>
		
		<div class="collapse navbar-collapse" id="navbarNavDropdown">
 			<ul class="navbar-nav mr-auto">
				<% if (session.getAttribute("APP_UTILISATEUR") != null) { %>
				<li class="nav-item active">
					<span class="text-white  nav-link mr-auto">Hello <%= ((Utilisateur) session.getAttribute("APP_UTILISATEUR")).getPrenom() %></span>
			    </li>
				<li class="nav-item my-2 my-lg-0">
					<a class="nav-link" href="<%= request.getContextPath() %>/AuthenticationController?action=logOut">Disconnect</a>
				</li>
				<% } else { %> 
			    
			    <li class="nav-item my-2 my-lg-0">
					<a class="nav-link" href="<%= request.getContextPath() %>/AuthenticationController?action=showLoginForm">Log in</a>
			    </li>
			    <% } %> 
		    	<li class="nav-item dropdown">
			        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			          Actions
			        </a>
			        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
			          <a class="dropdown-item" href="<%= request.getContextPath() %>/AuteurController?action=list">Authors</a>
			          <a class="dropdown-item" href="<%= request.getContextPath() %>/LivreController?action=list">Books</a>
			        </div>
		      	</li>
			</ul>
		</div>
	</nav>
</header>