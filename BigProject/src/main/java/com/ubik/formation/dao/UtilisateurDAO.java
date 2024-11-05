package com.ubik.formation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ubik.formation.entities.Utilisateur;
import com.ubik.formation.util.DatabaseConnection;
import com.ubik.formation.util.SQLConnectionCloser;

public class UtilisateurDAO {

	public static final String TABLENAME = "utilisateur";
	
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
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.prepareStatement("INSERT INTO " + TABLENAME + " (id, nom, prenom, login, pwd) VALUES (nextVal('utilisateur_id'), ?, ?, ?, ?)");

			statement.setString(1, utilisateur.getNom());
			statement.setString(2, utilisateur.getPrenom());
			statement.setString(3, utilisateur.getLogin());
			statement.setString(4, utilisateur.getPassword());
			
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
