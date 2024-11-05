package com.ubik.formation.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.ubik.formation.dao.AuteurDAO;
import com.ubik.formation.entities.Auteur;
import com.ubik.formation.util.DatabaseConnection;
import com.ubik.formation.util.SQLConnectionCloser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class AuteurController
 */
@WebServlet("/AuteurController")
public class AuteurController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int AUTEUR_PAR_PAGE = 5;
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");
		if (action == null) {
			listAuteur(request, response);
		} else {
			switch (action) {
				case "list" :
					listAuteur(request, response);
					break;
				case "add":
					addAuteur(request, response);
					break;
				case "update":
					updateAuteur(request, response);
					break;
				case "delete":
					deleteAuteur(request, response);
					break;
				case "form":
					showForm(request, response);
					break;
				default:
					listAuteur(request, response);
					break;
			}
		}
	}

	private void showForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Auteur auteur = request.getParameter("auteurId") != null 
				? AuteurDAO.get(Long.parseLong(request.getParameter("auteurId"))) 
				: null;
		request.setAttribute("auteur", auteur);
		request.getRequestDispatcher("/WEB-INF/views/FormAuteur.jsp").forward(request, response);

	}

	private void deleteAuteur(HttpServletRequest request, HttpServletResponse response) {
		Connection connection = null;
		String[] auteurIds = request.getParameterValues("auteurIds");

		
        try {
            connection = DatabaseConnection.getConnection();

            for (String id : auteurIds) {
            	AuteurDAO.delete(Long.parseLong(id), connection);
            }

            connection.commit();
            
    		request.setAttribute("status", "deleted");
    		listAuteur(request, response);

        } catch (Exception e) {
            try {
            	if (connection != null) {
            		connection.rollback();
            	}
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            request.setAttribute("errorMessage", e.getMessage());
            try {
                request.getRequestDispatcher("/WEB-INF/views/ErrorHandler.jsp").forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            SQLConnectionCloser.closeConnection(connection);
        }
        
	}

	private void updateAuteur(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Auteur auteur = new Auteur(
			Long.parseLong(request.getParameter("auteurId")),
			request.getParameter("lastName").trim(),
			request.getParameter("firstName").trim()
		);
		AuteurDAO.update(auteur);
		request.setAttribute("status", "updated");
		listAuteur(request, response);
	}

	private void addAuteur(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Auteur auteur = new Auteur(
			request.getParameter("lastName").trim(),
			request.getParameter("firstName").trim()
		);
		AuteurDAO.save(auteur);	
		request.setAttribute("status", "created");
		listAuteur(request, response);
	}

	private void listAuteur(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int page = request.getParameter("page") != null  ? Integer.parseInt(request.getParameter("page")) : 1;
        int start = (page - 1) * AUTEUR_PAR_PAGE;
        int totalAuteurs = AuteurDAO.getTotalAuteur();

        int totalPages = (int) Math.ceil((double) totalAuteurs / AUTEUR_PAR_PAGE);

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("page", page);
		request.setAttribute("auteurs", AuteurDAO.getAll(start, AUTEUR_PAR_PAGE));
		request.getRequestDispatcher("/WEB-INF/views/ListAuteur.jsp").forward(request, response);
	}

}
