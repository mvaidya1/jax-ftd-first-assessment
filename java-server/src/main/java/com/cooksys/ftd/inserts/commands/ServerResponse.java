package com.cooksys.ftd.inserts.commands;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServerResponse<T> {
	
	/**
	 * if true, an error was encountered and had no data returned.
	 * a message is optionally provided.
	 */
	private Boolean error;
	
	/**
	 * an optional message field
	 */
	private String message;
	
	/**
	 * the data returned by the server.
	 * will be missing in the event of an error.
	 */
	private T data;

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}