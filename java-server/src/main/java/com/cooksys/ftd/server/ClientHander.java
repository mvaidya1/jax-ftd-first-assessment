package com.cooksys.ftd.server;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import com.cooksys.ftd.inserts.dao.FileDao;
import com.cooksys.ftd.inserts.dao.UserDao;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ClientHander implements Runnable {
	private BufferedReader reader;
	private PrintWriter writer;
	
	private FileDao fileDao;
	private UserDao userDao;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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