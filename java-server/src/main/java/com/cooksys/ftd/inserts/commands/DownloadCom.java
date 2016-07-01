package com.cooksys.ftd.inserts.commands;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.cooksys.ftd.inserts.dao.FileUserDao;
import com.cooksys.ftd.inserts.model.db.FileUser;
import com.cooksys.ftd.inserts.model.db.UserFile;

public class DownloadCom extends AbstractCom {
	

	@Override
	public void executeCommand(String message, Map<String, Object> properties) throws JAXBException, SQLException {
		int searchFileId = Integer.parseInt(message);
		boolean fileExists = false;
		
		userFile = null;
		
		List<UserFile> userFiles = FileUserDao.getUserFileList(user);
		
		for (UserFile uf : userFiles) {
			if (uf.getFileId() == searchFileId) {
				fileExists = true;
			}
		}
		
		if (!fileExists) 
			return;
		
		userFile = fileDao.getFileById(searchFileId);
	
	}
}