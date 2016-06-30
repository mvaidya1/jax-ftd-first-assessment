package com.cooksys.ftd.inserts.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.cooksys.ftd.inserts.model.db.User;

public class UserDao extends AbstractDao {
	private static Logger log = LoggerFactory.getLogger(UserDao.class);
	public User createUser(User user) {

		try {
			String sql = "insert into user (id, username, password) values (?, ?, ?)";
			PreparedStatement stmt = this.getConn().prepareStatement(sql);
			stmt.setInt(1, user.getUserId());
			stmt.setString(2, user.getUsername());
			stmt.setString(3, user.getPassword());
			ResultSet rs = stmt.executeQuery();
			log.info("{} - {} - {}", rs.getInt("id"), rs.getString("username"),
					rs.getString("password"));
	}catch (SQLException e) {
		log.error("Something went wrong :/", e);
	}
		return user;
	}	
		public Optional<User> getUserByUsername(String username) {
			return null; // TODO
		}
	
}
