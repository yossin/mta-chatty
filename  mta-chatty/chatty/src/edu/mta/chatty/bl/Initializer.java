package edu.mta.chatty.bl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import edu.mta.chatty.util.DBHelper;

public class Initializer {
	private final static Logger logger = Logger.getLogger(Initializer.class.getName());
	private final DBHelper helper;
	public Initializer(DataSource ds){
		this.helper=new DBHelper(ds);
	}
	private boolean genericDbBatch(ServletContext context, String fileName) {
		try {
			InputStream is = context.getResourceAsStream(fileName);
			helper.executeBatchFromIS(is);
			return true;
		} catch (IOException e){
			logger.severe(String.format("unable to load file {0} for DB batch update. error is {1}", fileName, e));
		} catch (SQLException e){
			logger.severe(String.format("unable to perform sql batch update for file {0}. error is {1}", fileName, e));
		}
		return false;
	}

	public boolean dropTables(ServletContext context) {
		return genericDbBatch(context,"WEB-INF/sqlgen/drop.sql");
	}
	
	public boolean createTables(ServletContext context) {
		return genericDbBatch(context,"WEB-INF/sqlgen/create.sql");
	}

}
