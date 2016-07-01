package com.cooksys.ftd.inserts.commands;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.JAXBContextProperties;

import com.cooksys.ftd.inserts.model.db.UserFile;
import com.cooksys.ftd.inserts.dao.FileUserDao;
import com.cooksys.ftd.inserts.model.db.FileUser;

public class UploadCom extends AbstractCom {

	@Override
	public void executeCommand(String message, Map<String, Object> properties) throws JAXBException, SQLException {
		JAXBContext jc = JAXBContext.newInstance(new Class[] { UserFile.class }, properties);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setProperty(JAXBContextProperties.MEDIA_TYPE, "application/json");
		message = "{\"UserFile\":" + message + "}";
		
		UserFile newUserFile = (UserFile)unmarshaller.unmarshal(new StringReader(message));
		
		if (user.getUserId() == -1) {
			userFile = null;
			return;
		}
		
		String serverPath = generateFilePath(newUserFile);
		newUserFile.setFilePath(serverPath);
		
		userFile = fileDao.createFile(newUserFile);
		if (userFile.getFileId() == -1) {
			userFile = new UserFile(-1, "invalid", "invalid");
			return;
		}
	}
	
	private String generateFilePath(UserFile newFileD) {
		String filepath = newFileD.getFilePath();
		
		int delim = getDelimiter(filepath);
		
		while (filepath.indexOf(':') < filepath.indexOf(delim) && filepath.indexOf(':') != -1) {
			filepath = filepath.substring(filepath.indexOf(delim));
		}
		
		return "C:/" + user.getUsername() + filepath;
	}
	
	private int getDelimiter(String filepath) {
		int delim = '/';
		if (filepath.indexOf('/') == -1)
			delim = '\\';
		return delim;
	}
}