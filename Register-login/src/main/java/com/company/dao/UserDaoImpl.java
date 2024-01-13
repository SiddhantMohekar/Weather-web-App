package com.company.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.company.util.DBUtil;

public class UserDaoImpl implements UserDao{
	
	

	@Override
	public boolean isValidUser(String username, String password) {
		String query = "SELECT * FROM user WHERE username = ? AND password = ?";
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			System.out.println("Quey run ho gyi");
			
			return resultSet.next();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean addUser(User user) {
		 String query1 = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";
		
		 try (Connection connection = DBUtil.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(query1)) {

	            preparedStatement.setString(1, user.getUsername());
	            preparedStatement.setString(2, user.getEmail());
	            preparedStatement.setString(3, user.getPassword());

	            int rows = preparedStatement.executeUpdate();
	            
	            System.out.println("user added sucessfully");

	            return rows > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	}

	

	

}
