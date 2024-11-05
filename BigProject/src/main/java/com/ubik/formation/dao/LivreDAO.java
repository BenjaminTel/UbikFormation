package com.ubik.formation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ubik.formation.entities.Auteur;
import com.ubik.formation.entities.Livre;
import com.ubik.formation.util.DatabaseConnection;
import com.ubik.formation.util.SQLConnectionCloser;

public class LivreDAO{

	public static final String TABLENAME = "livre";
	
	public static Livre get(Long id) {
		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement(
				"SELECT l.id, l.auteurId, a.nom, a.prenom, l.titre, l.dateDeParution "
				+ "FROM "+ TABLENAME + " as l "
				+ "INNER JOIN " + AuteurDAO.TABLENAME + " as a ON a.id = l.auteurId "
				+ "WHERE l.id = ?"  
			);
		){		
			statement.setLong(1, id);
			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				return new Livre(
					rs.getLong("id"),
					new Auteur(rs.getLong("auteurID"), rs.getString("nom"), rs.getString("prenom")),
					rs.getString("titre"),
					rs.getTimestamp("dateDeParution").toLocalDateTime()
				);
			}
		} catch (SQLException e){
			
		}
		return null;
	}
	
	public static List<Livre> getAll(int start, int total, String orderBy, String sortOrder) {
		
	    List<String> validOrderByColumns = Arrays.asList("id", "auteurId", "titre", "dateDeParution");
	    List<String> validSortOrders = Arrays.asList("ASC", "DESC");

	    String livreRequest = "SELECT l.id, l.auteurId, a.nom, a.prenom, l.titre, l.dateDeParution "
	            + "FROM " + TABLENAME + " AS l "
	            + "INNER JOIN " + AuteurDAO.TABLENAME + " AS a ON a.id = l.auteurId ";
	    
	    if (validOrderByColumns.contains(orderBy) && validSortOrders.contains(sortOrder)) {
	        livreRequest += "ORDER BY " + orderBy + " " + sortOrder + " ";
	    }

	    livreRequest += "LIMIT ? OFFSET ?";
	    
		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement(livreRequest);
		){
			statement.setInt(1, total);
			statement.setInt(2, start);
			ResultSet rs = statement.executeQuery();

			List<Livre> livres = new ArrayList<>();
			while (rs.next()) {
				livres.add(
					new Livre(
						rs.getLong("id"),
						new Auteur(rs.getLong("auteurId"), rs.getString("nom"), rs.getString("prenom")),
						rs.getString("titre"),
						rs.getTimestamp("dateDeParution").toLocalDateTime()
					)
				);
			}
			return livres;
		} catch (SQLException e){
			
		}
		return null;
	}

	public static int getTotalLivre() {
		int total = 0;
		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT count(*) from " + TABLENAME);
		){
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			
		}
		return total;
	}
	
	public static void save(Livre livre) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.prepareStatement("INSERT INTO " + TABLENAME + " (id, auteurId, titre, dateDeParution) VALUES (nextVal('livre_id'), ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			statement.setLong(1, livre.getAuteur().getId());
			statement.setString(2, livre.getTitre());
			statement.setTimestamp(3, Timestamp.valueOf(livre.getDateDeParution()));
			
			int affectedRows = statement.executeUpdate();

			connection.commit();

	        if (affectedRows > 0) {
	            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    long generatedId = generatedKeys.getLong(1);
	                    livre.setId(generatedId);
	                } else {
	                    throw new SQLException("Insert failed, not generated key.");
	                }
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	        }
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			SQLConnectionCloser.closeConnection(new AutoCloseable[] {connection, statement});
		}
	}

	public static void update(Livre livre) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.prepareStatement("UPDATE " + TABLENAME + " SET auteurId = ?, titre = ?, dateDeParution = ? WHERE id = ?");
			
			statement.setLong(1, livre.getAuteur().getId());
			statement.setString(2, livre.getTitre());
			statement.setTimestamp(3, Timestamp.valueOf(livre.getDateDeParution()));
			statement.setLong(4, livre.getId());
			
			statement.executeUpdate();
			
			connection.commit();
		} catch (SQLException e){
			
		} finally {
			SQLConnectionCloser.closeConnection(new AutoCloseable[] {connection, statement});
		}
		
	}


	public static void delete(Long id) {
		String tagLivreRequest = "DELETE FROM tag_livre WHERE id_livre = ?";
		String livreRequest = "DELETE FROM " + TABLENAME + " WHERE id = ?";
		Connection connection = null;
		PreparedStatement tagLivreStatement = null;
		PreparedStatement livreStatement = null;
		try {
			connection = DatabaseConnection.getConnection();
			tagLivreStatement = connection.prepareStatement(tagLivreRequest);				
			livreStatement = connection.prepareStatement(livreRequest);
			
			tagLivreStatement.setLong(1, id);
			tagLivreStatement.executeUpdate();
			
			livreStatement.setLong(1, id);
			livreStatement.executeUpdate();
			
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
			SQLConnectionCloser.closeConnection(new AutoCloseable[] {connection, livreStatement, tagLivreStatement});
		}
	} 
	
	
}
