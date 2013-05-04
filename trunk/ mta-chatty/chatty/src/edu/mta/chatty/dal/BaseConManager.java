package edu.mta.chatty.dal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public abstract class BaseConManager {
	private static Logger logger = Logger.getLogger(BaseConManager.class.getName());

	private DataSource ds;
	BaseConManager(DataSource ds){
		this.ds = ds;
	}
	
	protected Connection getConnection(){
		try {
			Connection connection = ds.getConnection();
			return connection;
		} catch (SQLException e) {
			logger.throwing(BaseConManager.class.getName(), "getConnection", e);
			throw new RuntimeException(e);
		}
	}

	
	static void close(Connection connection){
		if (connection !=null){
			try {
				connection.close();
			} catch (SQLException e) {
				logger.warning("unable to close connection "+e);
			}
		}
		
	}

}
