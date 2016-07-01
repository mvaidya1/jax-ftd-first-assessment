package com.cooksys.ftd.inserts.dao;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cooksys.ftd.inserts.model.db.FileUser;
import com.cooksys.ftd.inserts.model.db.User;
import com.cooksys.ftd.inserts.model.db.UserFile;

public class FileUserDao extends AbstractDao {
		public UserFile createFile(UserFile fileD) throws SQLException {
			String findFileD = "SELECT * FROM file "
							 + "WHERE file_path LIKE ? ";
			
			// Check if file exists first. If so update it.
			PreparedStatement stmt = conn.prepareStatement(findFileD, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, fileD.getFilePath());
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) { // File already exists... update it.
				int fileId = rs.getInt("file_id");
				fileD.setFileId(fileId);
				String updateFileD = "UPDATE file "
								   + "SET file_path = ?, file_data = ? "
								   + "WHERE file_id = ? ";
				PreparedStatement updateStmt = conn.prepareStatement(updateFileD);
				updateStmt.setString(1, fileD.getFilePath());
				updateStmt.setString(2, fileD.getFileData());
				updateStmt.setInt(3, fileD.getFileId());
				updateStmt.executeUpdate();
			} else { // File does not exist... Create it.
				String createFileD = "INSERT INTO file (file_path, file_data) "
						   		   + "VALUES ( ?, ? ) ";
				PreparedStatement createStmt = conn.prepareStatement(createFileD, Statement.RETURN_GENERATED_KEYS );
				createStmt.setString(1, fileD.getFilePath());
				createStmt.setString(2, fileD.getFileData());
				int result = createStmt.executeUpdate();
				ResultSet createRs = createStmt.getGeneratedKeys();
				if (result == 0 || !createRs.next())
					return null;
				fileD.setFileId(createRs.getInt(1));
			}
			
			return fileD;
		}
		
		public UserFile getFileFromPath(String filepath) throws SQLException {
			String findFileD = "SELECT * FROM file "
					 	     + "WHERE filepath LIKE ? ";

			// Check if file exists first. If so update it.
			PreparedStatement stmt = conn.prepareStatement(findFileD, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, filepath);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) { // File exists in the database.
				int fileId = rs.getInt("file_id");
				String path = rs.getString("filepath");
				
			}
			
			return new UserFile(-1, "invalid", null);
		}

		public UserFile getFileById(int fileId) throws SQLException {
			if (fileId == -1) 
				return new UserFile(-1, "invalid", null);
			String findFileD = "SELECT * FROM file "
		 			 		 + "WHERE file_id = ? ";
			
			PreparedStatement stmt = conn.prepareStatement(findFileD, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, fileId);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt("file_id");
				String filepath = rs.getString("filepath");
				String file = rs.getString("data");
				
			}
			
			return null;
		}

		public static List<UserFile> getUserFileList(User user) throws SQLException {
			List<UserFile> userFiles = new ArrayList<UserFile>();
			
			String findUserFiles = "SELECT * FROM user_file "
			 		 			 + "WHERE user_id = ? ";
			
			PreparedStatement stmt = conn.prepareStatement(findUserFiles, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, user.getUserId());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				int userId = rs.getInt("user_id");
				int fileId = rs.getInt("file_id");
			}

			return userFiles;
		}
		
		}
	
