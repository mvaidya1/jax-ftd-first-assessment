package com.cooksys.ftd.inserts.commands;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.cooksys.ftd.inserts.dao.FileDao;
import com.cooksys.ftd.inserts.dao.FileUserDao;
import com.cooksys.ftd.inserts.dao.UserDao;
import com.cooksys.ftd.inserts.model.db.User;
import com.cooksys.ftd.inserts.model.db.UserFile;

public class AbstractCom {
	protected String filepath;
	protected String username;
	protected UserDao userDao;
	protected FileDao fileDao;
	protected FileUserDao fileUserDao;
	
	// Variables affected by individual execute commands
	protected User user;
	protected UserFile userFile;
	protected List<UserFile> userFiles;
	
	public AbstractCom() {
		super();
	}
	
	public void executeCommand(String message, Map<String, Object> properties) throws JAXBException, SQLException {
		// implement in all commands
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public FileDao getFileDao() {
		return fileDao;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public FileUserDao getFileUserDao() {
		return fileUserDao;
	}

	public void setFileUserDao(FileUserDao fileUserDao) {
		this.fileUserDao = fileUserDao;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserFile getUserFile() {
		return userFile;
	}

	public void setUserFile(UserFile userFile) {
		this.userFile = userFile;
	}

	public List<UserFile> getUserFiles() {
		return userFiles;
	}

	public void setUserFiles(List<UserFile> userFiles) {
		this.userFiles = userFiles;
	}
	
}
