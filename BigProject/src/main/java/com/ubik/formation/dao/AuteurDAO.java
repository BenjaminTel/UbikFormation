package com.ubik.formation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ubik.formation.entities.Auteur;
import com.ubik.formation.util.DatabaseConnection;
import com.ubik.formation.util.SQLConnectionCloser;

public class AuteurDAO{

	public static final String TABLENAME = "auteur";
	
	public static Auteur get(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.prepareStatement("SELECT * FROM "+ TABLENAME + " WHERE id =? ORDER BY id ASC");
			statement.setLong(1, id);
			
			rs = statement.executeQuery();
			Auteur auteur = new Auteur();
			while (rs.next()) {
				auteur.setId(rs.getLong("id"));
				auteur.setNom(rs.getString("nom"));
				auteur.setPrenom(rs.getString("prenom"));
			}
			return auteur;
		} catch (SQLException e){

		} finally {
			SQLConnectionCloser.closeConnection(new AutoCloseable[] {connection, statement, rs});
		}
		
		return null;
	}

	public static List<Auteur> getAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			connection = DatabaseConnection.getConnection();

			statement = connection.prepareStatement("SELECT id, nom, prenom FROM "+ TABLENAME);
			
			rs = statement.executeQuery();

			List<Auteur> auteurs = new ArrayList<>();
			while (rs.next()) {
				auteurs.add(new Auteur(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom")));
			}
			return auteurs;
		} catch (SQLException e){
			
		} finally {
			SQLConnectionCloser.closeConnection(new AutoCloseable[] {connection, statement, rs});
		}
		
		return null;
	}
	
	public static List<Auteur> getAll(int start, int total) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
		
			statement = connection.prepareStatement(
					"SELECT id, nom, prenom "
							+ "FROM "+ TABLENAME +" "
							+ "LIMIT ? "
							+ "OFFSET ?");
			statement.setInt(1, total);
			statement.setInt(2, start);
			rs = statement.executeQuery();
			List<Auteur> auteurs = new ArrayList<>();
			while (rs.next()) {
				auteurs.add(new Auteur(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom")));
			}
			return auteurs;
		} catch (SQLException e){
			
		} finally {
			SQLConnectionCloser.closeConnection(new AutoCloseable[]{connection ,rs, statement});
		}
		return null;
	}
	
	public static int getTotalAuteur() {
		int total = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();

			statement = connection.prepareStatement("SELECT count(*) from " + TABLENAME);
			rs = statement.executeQuery();
			while (rs.next()) {
				total = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			
		} finally {
			SQLConnectionCloser.closeConnection(new AutoCloseable[]{connection ,rs, statement});
		}
		return total;
	}

	public static void save(Auteur auteur) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.prepareStatement("INSERT INTO " + TABLENAME + " (id, nom, prenom) VALUES (nextVal('auteur_id'), ?, ?)");
	
			statement.setString(1, auteur.getNom());
			statement.setString(2, auteur.getPrenom());
			
			statement.executeUpdate();

			connection.commit();

		} catch (SQLException e){
			try {
				if (connection != null) {
					connection.rollback();					
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			SQLConnectionCloser.closeConnection(new AutoCloseable[]{connection, statement});
		}
	}

	public static void update(Auteur auteur) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.prepareStatement("UPDATE " + TABLENAME + " SET nom = ?, prenom = ? WHERE id = ?");
			statement.setString(1, auteur.getNom());
			statement.setString(2, auteur.getPrenom());
			statement.setLong(3, auteur.getId());
			
			statement.executeUpdate();
			
			connection.commit();

		} catch (SQLException e){
			try {
				if (connection != null) {
					connection.rollback();					
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			SQLConnectionCloser.closeConnection(new AutoCloseable[]{connection, statement});
		}
	}


	public static void delete(Long id, Connection connection) throws Exception {
	    String tagLivreRequest = "DELETE FROM tag_livre WHERE id_livre IN (SELECT id FROM " + LivreDAO.TABLENAME + " WHERE auteurId = ?)";
		String livreRequest = "DELETE FROM " + LivreDAO.TABLENAME + " WHERE auteurId = ? ";
		String auteurRequest = "DELETE FROM " + TABLENAME + " WHERE id= ?";

		try (
			PreparedStatement tagLivreStatement = connection.prepareStatement(tagLivreRequest);
			PreparedStatement livreStatement = connection.prepareStatement(livreRequest);
			PreparedStatement auteurStatement = connection.prepareStatement(auteurRequest);		
		) {
			tagLivreStatement.setLong(1, id);
			tagLivreStatement.executeUpdate();
			
			livreStatement.setLong(1, id);
			livreStatement.executeUpdate();
			
			auteurStatement.setLong(1, id);
			auteurStatement.executeUpdate();
		}
	}

}
