package com.cooksys.ftd.inserts.commands;

import com.cooksys.ftd.inserts.model.db.User;
import com.cooksys.ftd.inserts.model.db.UserFile;

public class addFile {
	private UserFile file;
	private User user;
	public UserFile getFile() {
		return file;
	}
	public void setFile(UserFile file) {
		this.file = file;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
