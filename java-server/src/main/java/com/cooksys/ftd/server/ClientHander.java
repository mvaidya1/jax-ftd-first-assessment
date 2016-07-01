package com.cooksys.ftd.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.inserts.commands.Message;
import com.cooksys.ftd.inserts.dao.FileDao;
import com.cooksys.ftd.inserts.dao.UserDao;

public class ClientHander implements Runnable {
	
	private Logger log = LoggerFactory.getLogger(ClientHander.class);
	
	private BufferedReader reader;
	private PrintWriter writer;
	Map<String, Object> properties = new HashMap<String, Object>();
	private String serverId;
	private FileDao fileDao;
	private UserDao userDao;
	private Message message;
	private boolean close;
	
	
	@Override
	public void run() {
		properties.put("eclipselink.media-type", "application/json");
		message.generateServerId();
		
		log.info("Server ID is {}", serverId);
		close = false; // read commands and data from the client 
		String input = "";
		try {
			while (!close) {
				log.info("Waiting for client input...");
				
				Message message1 = new Message();
				
				log.info("clientMessage: data={}, message={}", message1.getContent(), message1.getCommand());
				
				switch (message1.getCommand()) {
				case "register": message.registerUser(message1);; break;
				case "login": message.loginUser(message1); break;
				default: 
				}
			}
		} catch (IOException | JAXBException e) {
			log.error("Error processing user input " + input + ".", e);
			writer.write("{\"response\":{\"message\":\"error\"}}");
			close = true;
		} catch (SQLException e) {
			log.error("Error retreiving information from SQL database.", e);
			writer.write("{\"response\":{\"message\":\"error\"}}");
			close = true;
		}
	}


	public BufferedReader getReader() {
		return reader;
	}


	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}


	public PrintWriter getWriter() {
		return writer;
	}


	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}


	public Map<String, Object> getProperties() {
		return properties;
	}


	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}


	public String getServerId() {
		return serverId;
	}


	public void setServerId(String serverId) {
		this.serverId = serverId;
	}


	public FileDao getFileDao() {
		return fileDao;
	}


	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}


	public UserDao getUserDao() {
		return userDao;
	}


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}