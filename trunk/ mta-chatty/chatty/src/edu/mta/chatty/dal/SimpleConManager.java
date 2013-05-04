package edu.mta.chatty.dal;

import java.sql.Connection;

import javax.sql.DataSource;

public class SimpleConManager extends BaseConManager{

	private Object lock = new Object();
	SimpleConManager(DataSource ds) {
		super(ds);
	}
	private Connection connection = null;
	
	public Connection connect(){
		synchronized (lock) {
			if (connection == null){
				connection = getConnection();
			}
			return connection;
		}
		
	}
	
	public void disconnect(){
		synchronized (lock) {
			if (connection != null){
				close(connection);
				connection=null;
			}
		}
	}

}
