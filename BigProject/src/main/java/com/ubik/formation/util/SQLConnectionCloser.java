package com.ubik.formation.util;

public class SQLConnectionCloser {

	
	public static void closeConnection(AutoCloseable... toClose) {
		for (AutoCloseable c : toClose) {
			try {
				if (c != null ) {
					c.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
