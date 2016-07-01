package com.cooksys.ftd.inserts.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.inserts.model.db.UserFile;
import com.cooksys.ftd.server.ClientHander;

public class FileDao extends AbstractDao {
	
	public UserFile createFile(UserFile fileD) throws SQLException {
		String findFileD = "SELECT * FROM file "
						 + "WHERE filepath LIKE ? ";
		
		// Check if file exists first. If so update it.
		PreparedStatement stmt = conn.prepareStatement(findFileD, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, fileD.getFilePath());
		ResultSet rs = stmt.executeQuery();
		
		if (rs.next()) { // File already exists... update it.
			int fileId = rs.getInt("file_id");
			fileD.setFileId(fileId);
			String updateFileD = "UPDATE file "
							   + "SET filepath = ?, data = ? "
							   + "WHERE file_id = ? ";
			PreparedStatement updateStmt = conn.prepareStatement(updateFileD);
			updateStmt.setString(1, fileD.getFilePath());
			updateStmt.setString(2, fileD.getFileData());
			updateStmt.setInt(3, fileD.getFileId());
			updateStmt.executeUpdate();
		} else { // File does not exist... Create it.
			String createFileD = "INSERT INTO file (filepath, data) "
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
			String file = rs.getString("data");
			return new UserFile(fileId, path, file);
		}
		
		return new UserFile(-1, "invalid", "invalid");
	}

	public UserFile getFileById(int fileId) throws SQLException {
		if (fileId == -1) 
			return new UserFile(-1, "invalid", "invalid");
		String findFileD = "SELECT * FROM file "
	 			 		 + "WHERE file_id = ? ";
		
		PreparedStatement stmt = conn.prepareStatement(findFileD, Statement.RETURN_GENERATED_KEYS);
		stmt.setInt(1, fileId);
		ResultSet rs = stmt.executeQuery();
		
		if (rs.next()) {
			int id = rs.getInt("file_id");
			String filepath = rs.getString("filepath");
			String file = rs.getString("data");
			return new UserFile(id, filepath, file);
		}
		
		return null;
	}
}