package edu.mta.chatty.bl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import edu.mta.chatty.dal.DataInitializer;
import edu.mta.chatty.util.DBHelper;

public class Initializer {
	private final static Logger logger = Logger.getLogger(Initializer.class.getName());
	private final DBHelper helper;
	private final DataInitializer dataInitializer;

	public Initializer(DataSource ds){
		this.helper=new DBHelper(ds);
		this.dataInitializer = new DataInitializer(ds);
	}
	private boolean genericDbBatch(ServletContext context, String fileName) {
		try {
			InputStream is = context.getResourceAsStream(fileName);
			helper.executeBatchFromIS(is);
			return true;
		} catch (IOException e){
			logger.severe(String.format("unable to load file %s for DB batch update. error is %s", fileName, e));
		} catch (SQLException e){
			logger.severe(String.format("unable to perform sql batch update for file %s. error is %s", fileName, e));
		}
		return false;
	}

	public boolean dropTables(ServletContext context) {
		return genericDbBatch(context,"WEB-INF/sqlgen/drop.sql");
	}
	
	public boolean createTables(ServletContext context) {
		return genericDbBatch(context,"WEB-INF/sqlgen/create.sql");
	}
		
	
	public boolean loadTestData(ServletContext context){
		try {
			return dataInitializer.loadInitData(context);
		}catch (Exception e){
			logger.severe(String.format("unable to load test data into DB. error is %s", e));
			return false;
		}
	}

}
