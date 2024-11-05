package com.ubik.formation.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.ubik.formation.entities.Tag;
import com.ubik.formation.util.DatabaseConnection;
import com.ubik.formation.util.SQLConnectionCloser;

public class TagDAO {
	public static final String TABLENAME = "tag";

	public static Tag getById(Long id) {
		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+ TABLENAME + " WHERE id =? ORDER BY id ASC");
		){
			statement.setLong(1, id);
			
			ResultSet rs = statement.executeQuery();
			Tag tag = new Tag();;
			while (rs.next()) {
				tag = new Tag(rs.getLong("id"), rs.getString("libelle"));
			}
			return tag;
		} catch (SQLException e){

		}
		return null;
	}
	
	public static Tag getByLibelle(String libelle) {
		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT id, libelle FROM "+ TABLENAME + " WHERE libelle =?");
		){
			statement.setString(1, libelle);
			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				return new Tag(rs.getLong("id"), rs.getString("libelle"));
			}
		} catch (SQLException e){

		}
		return null;
	}
	
	public static void save(Tag tag) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.prepareStatement("INSERT INTO "+ TABLENAME + " (id, libelle)  VALUES (nextVal('tag_id'), ?)", Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, tag.getLibelle());
			
			int affectedRows = statement.executeUpdate();

			connection.commit();
	        if (affectedRows > 0) {
	            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    long generatedId = generatedKeys.getLong(1);
	                    tag.setId(generatedId);
	                } else {
	                    throw new SQLException("Insert failed, not generated key.");
	                }
	            }
	        }
		} catch (SQLException e){
			try {			
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			SQLConnectionCloser.closeConnection(new AutoCloseable[] {connection, statement});
		}
	}
	
	public static Set<String> getTagsByLivreId(Long id_livre) {
		Set<String> tags = new HashSet<>();

		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement(
				"SELECT t.id, t.libelle "
				+ "FROM " + TABLENAME + " as t "
				+ "INNER JOIN tag_livre as tl ON t.id = tl.id_tag "  
				+ "WHERE tl.id_livre = ?"
			);
		){
			statement.setLong(1, id_livre);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				tags.add(rs.getString("libelle"));
			}
		} catch (SQLException e){
			
		}
		return tags;
	}
	
	private static void linkToLivre(Tag tag, Long id_livre) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.prepareStatement("INSERT INTO tag_livre (id_livre, id_tag)  VALUES (?, ?)");

			statement.setLong(1, id_livre);
			statement.setLong(2, tag.getId());
			
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
			SQLConnectionCloser.closeConnection(new AutoCloseable[] {connection, statement});
		}
	}
	
	public static void createOrGetToLink(String libelle, Long id_livre) {
		try {
			Tag tag = getByLibelle(libelle);
		
		
			if (tag == null) {
				tag = new Tag(libelle);
				save(tag);
			}
			
			linkToLivre(tag, id_livre);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteLinkToLivre(String libelle, Long id_livre) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.prepareStatement(
					"DELETE tl "
					+ "FROM tag_livre tl "
					+ "JOIN tag t ON t.id = tl.id_tag "
					+ "WHERE tl.id_livre= ? AND t.libelle= ?"
			);
		
			statement.setLong(1, id_livre);
			statement.setString(2, libelle);
			
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
			SQLConnectionCloser.closeConnection(new AutoCloseable[] {connection, statement});
		}
	}
}
