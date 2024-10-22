package com.ubik.formation.util;

import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;


@WebFilter("/*")
public class AccessFilter extends HttpFilter {
       

	private static final long serialVersionUID = 1L;


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpSession session = req.getSession(true);
		
        boolean isLoggedIn = (session != null && session.getAttribute("APP_UTILISATEUR") != null);

    	if (isLoggedIn || req.getRequestURI().contains("AuthenticationController")) {
            chain.doFilter(request, response);
        } else {
    		res.sendRedirect("AuthenticationController?action=showLoginForm");
        }     
	}

}
