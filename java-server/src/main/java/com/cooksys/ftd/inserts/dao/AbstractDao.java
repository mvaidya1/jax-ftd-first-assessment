package com.cooksys.ftd.inserts.dao;

import java.sql.Connection;

public abstract class AbstractDao {
	
	protected static Connection conn;

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

}
