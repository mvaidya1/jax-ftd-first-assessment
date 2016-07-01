package com.cooksys.ftd.inserts.model.db;

import javax.xml.bind.annotation.XmlRootElement;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
@XmlRootElement(name = "userFile")
public class UserFile {
@XmlElement(name = "fileId")
private Integer fileId;
@XmlElement(name = "filePath")
private String filePath;
@XmlElement(name = "fileData")
private String fileData;

public UserFile(int fileId, String filePath, String fileData) {
	// TODO Auto-generated constructor stub
	super();
	this.fileId = fileId;
	this.fileData = fileData;
	this.filePath = filePath;
}
public Integer getFileId() {
	return fileId;
}
public void setFileId(Integer fileId) {
	this.fileId = fileId;
}
public String getFilePath() {
	return filePath;
}
public void setFilePath(String filePath) {
	this.filePath = filePath;
}
public String getFileData() {
	return fileData;
}
public void setFileData(String fileData) {
	this.fileData = fileData;
}
@Override
public String toString() {
	return "UserFile [fileId=" + fileId + ", filePath=" + filePath + ", fileData=" + fileData + "]";
}
}
