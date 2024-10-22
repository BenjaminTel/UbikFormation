package com.ubik.formation.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import com.ubik.formation.dao.UtilisateurDAO;
import com.ubik.formation.entities.Utilisateur;
/**
 * Servlet implementation class Authentication
 */
@WebServlet("/AuthenticationController")
public class AuthenticationController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null) {
			showLoginForm(request, response);
		} else {
			switch (action) {
				case "logIn" :
					logIn(request, response);
					break;
				case "logOut":
					logOut(request, response);
					break;
				case "showLoginForm":
					showLoginForm(request, response);
					break;
				default:
					showLoginForm(request, response);
					break;
			}
		}
	}
	
	private void logIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String  hashedPassword = BCrypt.hashpw(request.getParameter("password"), Utilisateur.SALT);
		Utilisateur utilisateur = UtilisateurDAO.get(request.getParameter("login"), hashedPassword);
		if (utilisateur != null) {
			request.getSession(true).setAttribute("APP_UTILISATEUR", utilisateur);
			request.getRequestDispatcher("WEB-INF/views/Index.jsp").forward(request, response);
		} else {
			request.setAttribute("error", "Authentication failed.");
			request.getRequestDispatcher("WEB-INF/views/Login.jsp").forward(request, response);			
		}
	}
	
	private void logOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession(true).removeAttribute("APP_UTILISATEUR");
		request.getRequestDispatcher("WEB-INF/views/Login.jsp").forward(request, response);			
	}

	private void showLoginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession(true).getAttribute("APP_UTILISATEUR") != null) {
			request.getRequestDispatcher("WEB-INF/views/Index.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("WEB-INF/views/Login.jsp").forward(request, response);
		}
	}
}
