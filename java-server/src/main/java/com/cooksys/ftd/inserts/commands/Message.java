package com.cooksys.ftd.inserts.commands;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.inserts.dao.FileDao;
import com.cooksys.ftd.inserts.dao.FileUserDao;
import com.cooksys.ftd.inserts.dao.UserDao;
import com.cooksys.ftd.inserts.model.db.User;
import com.cooksys.ftd.inserts.model.db.UserFile;

@XmlRootElement
public class Message {

private Logger log = LoggerFactory.getLogger(Message.class);
	
	private BufferedReader reader;
	private PrintWriter writer;
	private String command;
	private String content;
	Map<String, Object> properties = new HashMap<String, Object>();
	private String serverId;
	private UserDao userDao;
	private FileDao fileDao;
	private FileUserDao userFileDao;
	
	private Map<String, Object> args = new HashMap<>();
	
	public String getCommand() {
		return this.command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, Object> getArgs() {
		return args;
	}

	public void setArgs(Map<String, Object> args) {
		this.args = args;
	}
	
	public Message getClientMessage() throws IOException, JAXBException {
		String input = reader.readLine();
		log.info("Input: {}", input);
		JAXBContext jc = JAXBContext.newInstance(new Class[] { Message.class }, properties);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setProperty(JAXBContextProperties.MEDIA_TYPE, "application/json");
		
		return (Message)unmarshaller.unmarshal(new StringReader(input));
	}
	
	public void registerUser(Message clientMessage) throws JAXBException, SQLException {
		ServerResponse<User> response = new ServerResponse<>();
		response.setMessage("User has been sucessfully registered!");
		
		AbstractCom regCmd = new GetUserByUsername();
		regCmd.setUserDao(userDao);
		regCmd.executeCommand(clientMessage.getContent(), properties);
		User newUser = regCmd.getUser();
		response.setData(newUser);
		
		if (newUser == null) {
			String message = "Unable to enter user into database.";
			response.setMessage(message);
			log.error(message);
		}
		else {
			if (newUser.getUserId() == -1) {
				String message = "Username already exists.";
				response.setMessage(message);
				log.info(message);
			} else
				log.info("User {} has been sucessfully registered!", newUser.getUsername());
		}
		
		sendResponse(response);
	}
	
	public void loginUser(Message clientMessage) throws JAXBException, SQLException, IOException {
		generateServerId();
		
		ServerResponse<String> response = new ServerResponse<>();
		String message = "Login credentials are incorrect.";
		response.setMessage(message);
		response.setData("invalid");
		
		AbstractCom logCmd = new GetUserByUsername();
		logCmd.setUserDao(userDao);
		logCmd.executeCommand(clientMessage.getContent(), properties);
		User newUser = logCmd.getUser();
		
		if (newUser.getUserId() == -1) {
			log.info(message);
			sendResponse(response);
			return;
		} 
		
		ServerResponse<User> checkPwd = new ServerResponse<>();
		checkPwd.setMessage("checkPass");
		checkPwd.setData(newUser);
		sendResponse(checkPwd);	// Send user/hashed pass to check with client side pass
		
		Message passwordCheck = getClientMessage();
		
		if (passwordCheck.getCommand().equals("success")) {
			message = "Login successful!";
			response.setMessage(message);
			response.setData(serverId);
			log.info(message);
		}
		
		sendResponse(response);
	}
	public void uploadFileD(String connID, String data) throws JAXBException, SQLException {
		ServerResponse<UserFile> response = new ServerResponse<>();
		response.setData(new UserFile(-1, "invalid", null));
		
		int delim = connID.indexOf('*');
		if (delim == -1) {
			response.setMessage("*error*Invalid session ID detected");
			sendResponse(response);
			return;
		}
		
		String username = connID.substring(0, delim);
		User tempUser = userDao.getUserByUsername(username);
		
		AbstractCom upCmd = new UploadCom();
		upCmd.setUser(tempUser);
		upCmd.setFileUserDao(userFileDao);
		upCmd.setFileDao(fileDao);
		upCmd.executeCommand(data, properties);
		
		if (upCmd.getUserFile() == null)
			response.setMessage("Error when reading file.");
		else if (upCmd.getUserFile().getFileId() == -1)
			response.setMessage("Error when storing file to database.");
		else if (upCmd.getUserFile().getFileId() == null)
			response.setMessage("Error when setting up user file relashionship.");
		else if (upCmd.getUserFile().getFileId() == -1 || upCmd.getUserFile().getFileId() == -1)
			response.setMessage("Error when writing user file relashionship.");
		else {
			response.setData(upCmd.getUserFile());
			response.setMessage("File has been sucessfully written.");
		}
		sendResponse(response);
	}
	
	public void downloadFileD(String connID, String data, Object fileDDao) throws JAXBException, SQLException {
		ServerResponse<UserFile> response = new ServerResponse<>();
		response.setData(new UserFile(-1, "null", null));
		
		int delim = connID.indexOf('*');
		if (delim == -1) {
			response.setMessage("Invalid session ID detected");
			sendResponse(response);
			return;
		}
		
		String username = connID.substring(0, delim);
		User tempUser = userDao.getUserByUsername(username);
		
		AbstractCom downCmd = new DownloadCom();
		downCmd.setUser(tempUser);
		downCmd.setFileDao(fileDao);
		downCmd.setFileUserDao(userFileDao);
		downCmd.executeCommand(data, properties);
		
		FileDao resultFileD = downCmd.getFileDao();
		
		
		sendResponse(response);
	}
	public void sendResponse(ServerResponse<?> response) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(new Class[] { ServerResponse.class }, properties);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(JAXBContextProperties.MEDIA_TYPE, "application/json");
		
		marshaller.marshal(response, writer);
		writer.flush();
	}
	
	public void generateServerId() {
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890`-=~!@#$%^&*()_+[]\\{}|;':\",./<>?".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		serverId = sb.toString();
		log.info("Generated session id: {}", serverId);
	}
	
}
