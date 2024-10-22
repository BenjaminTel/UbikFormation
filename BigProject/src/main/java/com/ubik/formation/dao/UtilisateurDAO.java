package com.ubik.formation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ubik.formation.entities.Utilisateur;
import com.ubik.formation.util.DatabaseConnection;

public class UtilisateurDAO {

	public static final String TABLENAME = "utilisateur";
	
	public static Utilisateur get(Long id) {
		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+ TABLENAME + " WHERE id =? ORDER BY id ASC");
		){
			statement.setLong(1, id);
			
			ResultSet rs = statement.executeQuery();
			Utilisateur utilisateur = new Utilisateur();
			while (rs.next()) {
				utilisateur.setId(rs.getLong("id"));
				utilisateur.setNom(rs.getString("nom"));
				utilisateur.setPrenom(rs.getString("prenom"));
				utilisateur.setLogin(rs.getString("login"));
				utilisateur.setPassword(rs.getString("password"));
			}
			return utilisateur;
		} catch (SQLException e){

		}
		return null;
	}
	
	public static Utilisateur get(String login, String hashedPassword) {
		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+ TABLENAME + " WHERE login =? AND pwd = ?");
		){
			statement.setString(1, login);
			statement.setString(2, hashedPassword);
			
			ResultSet rs = statement.executeQuery();
			Utilisateur utilisateur = null;
			while (rs.next()) {
				utilisateur = new Utilisateur(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("pwd"));
			}
			return utilisateur;
		} catch (SQLException e){

		}
		return null;
	}
	
	public static void save(Utilisateur utilisateur) {
		try (
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLENAME + " (id, nom, prenom, login, pwd) VALUES (nextVal('utilisateur_id'), ?, ?, ?, ?)");
		){
			statement.setString(1, utilisateur.getNom());
			statement.setString(2, utilisateur.getPrenom());
			statement.setString(3, utilisateur.getLogin());
			statement.setString(4, utilisateur.getPassword());
			
			statement.executeUpdate();
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
}
