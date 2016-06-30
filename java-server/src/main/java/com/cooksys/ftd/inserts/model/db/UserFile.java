package com.cooksys.ftd.inserts.model.db;

public class UserFile {
private Integer fileId;
private String filePath;
private String fileData;

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
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((fileData == null) ? 0 : fileData.hashCode());
	result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
	result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	UserFile other = (UserFile) obj;
	if (fileData == null) {
		if (other.fileData != null)
			return false;
	} else if (!fileData.equals(other.fileData))
		return false;
	if (fileId == null) {
		if (other.fileId != null)
			return false;
	} else if (!fileId.equals(other.fileId))
		return false;
	if (filePath == null) {
		if (other.filePath != null)
			return false;
	} else if (!filePath.equals(other.filePath))
		return false;
	return true;
}
@Override
public String toString() {
	return "UserFile [fileId=" + fileId + ", filePath=" + filePath + ", fileData=" + fileData + "]";
}


}
