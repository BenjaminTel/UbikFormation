package com.ubik.formation.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ubik.formation.dao.AuteurDAO;
import com.ubik.formation.dao.LivreDAO;
import com.ubik.formation.dao.TagDAO;
import com.ubik.formation.entities.Livre;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class LivreController
 */
@WebServlet("/LivreController")
public class LivreController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIVRE_PAR_PAGE = 5;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			listLivre(request, response);
		} else {
			switch (action) {
				case "list" :
					listLivre(request, response);
					break;
				case "add":
					addLivre(request, response);
					break;
				case "update":
					updateLivre(request, response);
					break;
				case "delete":
					deleteLivre(request, response);
					break;
				case "form":
					showForm(request, response);
					break;
				default:
					listLivre(request, response);
					break;
			}

		}
	}

	private void showForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("auteurs", AuteurDAO.getAll());
		String id_livre = request.getParameter("livreId");
		Livre livre =  id_livre != null 
				? LivreDAO.get(Long.parseLong(request.getParameter("livreId"))) 
				: null;
		request.setAttribute("livre", livre);
		
		Set<String> tags = request.getParameter("livreId") != null 
				? TagDAO.getTagsByLivreId(Long.parseLong(id_livre))
				: null;
		request.setAttribute("tags", tags);
		request.getRequestDispatcher("/WEB-INF/views/FormLivre.jsp").forward(request, response);

	}

	private void deleteLivre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LivreDAO.delete(Long.parseLong(request.getParameter("livreId")));
		listLivre(request, response);
	}

	private void updateLivre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long livreId = Long.parseLong(request.getParameter("livreId"));
		Livre livre = new Livre(
			livreId,
			AuteurDAO.get(Long.parseLong(request.getParameter("auteurId"))),
			request.getParameter("titre").trim(),
			LocalDateTime.parse(request.getParameter("dateDeParution"))
		);
		LivreDAO.update(livre);
		
		
		String formTags = request.getParameter("tags");

		if (formTags != null && formTags != "") {			
			Set<String> formTagsSet = new HashSet<>(Arrays.asList(formTags.split(",")));

			Set<String> currentTagsSet = TagDAO.getTagsByLivreId(livreId);
			if (currentTagsSet.size() > 0) {
				Set<String> toUnlink = new HashSet<>(currentTagsSet);
				toUnlink.removeAll(formTagsSet);
				for (String tagLibelle : toUnlink) {
					TagDAO.deleteLinkToLivre(tagLibelle, livreId);
				}
			}
			
			Set<String> toAdd = new HashSet<>(formTagsSet);
			toAdd.removeAll(currentTagsSet);
			for (String tagLibelle : toAdd) {
				TagDAO.createOrGetToLink(tagLibelle.trim(), livreId);
			}
		}

		request.setAttribute("status", "updated");
		listLivre(request, response);
	}

	private void addLivre(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Livre livre = new Livre(
			AuteurDAO.get(Long.parseLong(request.getParameter("auteurId"))),
			request.getParameter("titre").trim(),
			LocalDateTime.parse(request.getParameter("dateDeParution"))
		);
		LivreDAO.save(livre);	

		String formTags = request.getParameter("tags");
		if (formTags != null) {
			String[] tags = formTags.split(",");
			for (String tag : tags) {
				TagDAO.createOrGetToLink(tag.trim(), livre.getId());
			}
		}
		request.setAttribute("status", "created");
		listLivre(request, response);
	}

	private void listLivre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int page = request.getParameter("page") != null  
				? Integer.parseInt(request.getParameter("page")) 
				: 1;
        int start = (page - 1) * LIVRE_PAR_PAGE;
        int totalLivres = LivreDAO.getTotalLivre();
        
        int totalPages = (int) Math.ceil((double) totalLivres / LIVRE_PAR_PAGE);

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("page", page);
        
        String sortBy = request.getParameter("sortBy");
                
        Comparator<Livre> comparator = null;

        if (sortBy != null) {
        	switch (sortBy) {
	        	case "date":
	        		comparator = Comparator.comparing(Livre::getDateDeParution);
	        		if (request.getParameter("order") != null && request.getParameter("order").equals("desc")) {
	        			comparator = comparator.reversed();
	        		}
	        		break;
	        	default:
	        		break;
        	}
        }
        
        List<Livre> livres =  LivreDAO.getAll(start, LIVRE_PAR_PAGE);
        if (comparator != null) {
        	livres.sort(comparator);
        }
		request.setAttribute("livres", livres);
		request.getRequestDispatcher("/WEB-INF/views/ListLivre.jsp").forward(request, response);
	}

}
