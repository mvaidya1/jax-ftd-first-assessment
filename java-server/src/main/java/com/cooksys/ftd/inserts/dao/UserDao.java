package com.cooksys.ftd.inserts.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.inserts.model.db.User;

public class UserDao extends AbstractDao {
	private static Logger log = LoggerFactory.getLogger(UserDao.class);
	public User createUser(User user) {
		try {
			String sql = "insert into user (username, password) values (?, ?)";
			PreparedStatement stmt = this.getConn().prepareStatement(sql);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				user.setUserId(rs.getInt(1));
				
			}
			
	}catch (SQLException e) {
		log.error("Something went wrong :/", e);
	}
		return user;
	}
	public User getUserByUsername(String username) {
		User user = new User(-1, "null", "null");
		String findUser = "SELECT * FROM user "
						+ "WHERE username LIKE ? ";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(findUser);
		stmt.setString(1, username);
		ResultSet rs = stmt.executeQuery();
		
		if (rs.next()) {
			String name = rs.getString("username");
			String pass = rs.getString("password");
			int id = rs.getInt("user_id");
			
			user = new User(id, name, pass);
			log.info("User=id: {}, username: {}, password: {}", id, name, pass);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("We have encountered an error trying to get the user. {}", e);
		}
		return user;
	}
}
