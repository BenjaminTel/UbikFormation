package com.ubik.formation.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseConnection {

	 public static Connection getConnection() {
	        Connection connection = null;
	        try {
	            Context initContext = new InitialContext();
	            DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/postgres");
	            connection = ds.getConnection();
	        } catch (SQLException | NamingException e) {
	            e.printStackTrace();
	        }
	        return connection;
	    }
}
