package com.cooksys.ftd.inserts.commands;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.JAXBContextProperties;

import com.cooksys.ftd.inserts.model.db.User;

public class GetUserByUsername extends AbstractCom{
	
	public void executeCommand(String message, Map<String, Object> properties) throws JAXBException, SQLException {
		JAXBContext jc = JAXBContext.newInstance(new Class[] { User.class }, properties);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setProperty(JAXBContextProperties.MEDIA_TYPE, "application/json");
		message = "{\"user\":" + message + "}";
		
		User newUser = (User)unmarshaller.unmarshal(new StringReader(message));
		
		this.user = userDao.createUser(newUser);
	}
}
