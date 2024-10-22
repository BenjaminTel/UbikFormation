package com.ubik.formation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ubik.formation.entities.Auteur;
import com.ubik.formation.util.DatabaseConnection;

public class AuteurDAO{

	public static final String TABLENAME = "auteur";
	
	public static Auteur get(Long id) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try (
			Connection connection = DatabaseConnection.getConnection();
		){
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
			try {
				statement.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static List<Auteur> getAll() {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try (
			Connection connection = DatabaseConnection.getConnection();
		){
			statement = connection.prepareStatement("SELECT id, nom, prenom FROM "+ TABLENAME);
			
			rs = statement.executeQuery();

			List<Auteur> auteurs = new ArrayList<>();
			while (rs.next()) {
				auteurs.add(new Auteur(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom")));
			}
			return auteurs;
		} catch (SQLException e){
			
		} finally {
			try {
				statement.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static List<Auteur> getAll(int start, int total) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try (
			Connection connection = DatabaseConnection.getConnection();
		){
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
			try {
				statement.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static int getTotalAuteur() {
		int total = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try (
			Connection connection = DatabaseConnection.getConnection();
		){
			statement = connection.prepareStatement("SELECT count(*) from " + TABLENAME);
			rs = statement.executeQuery();
			while (rs.next()) {
				total = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			
		} finally {
			try {
				statement.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return total;
	}

	public static void save(Auteur auteur) {
		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLENAME + " (id, nom, prenom) VALUES (nextVal('auteur_id'), ?, ?)");
		){
			statement.setString(1, auteur.getNom());
			statement.setString(2, auteur.getPrenom());
			
			statement.executeUpdate();

		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static void update(Auteur auteur) {
		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("UPDATE " + TABLENAME + " SET nom = ?, prenom = ? WHERE id = ?");
		){
			statement.setString(1, auteur.getNom());
			statement.setString(2, auteur.getPrenom());
			statement.setLong(3, auteur.getId());
			
			statement.executeUpdate();
		} catch (SQLException e){
			
		}
	}


	public static void delete(Long id) {
		String livreRequest = "DELETE FROM " + LivreDAO.TABLENAME + " WHERE auteurId = ? ";
		String auteurRequest = "DELETE FROM " + TABLENAME + " WHERE id= ?";
		Connection connection = null;
		PreparedStatement livreStatement = null;
		PreparedStatement auteurStatement = null;
		try {
			connection = DatabaseConnection.getConnection();
			livreStatement = connection.prepareStatement(livreRequest);
			auteurStatement = connection.prepareStatement(auteurRequest);				
			connection.setAutoCommit(false);
			
			livreStatement.setLong(1, id);
			livreStatement.executeUpdate();
			
			auteurStatement.setLong(1, id);
			auteurStatement.executeUpdate();
			
			connection.commit();
			
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();					
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (livreStatement != null) {
					livreStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (auteurStatement != null) {
					auteurStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
