package edu.mta.chatty.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import edu.mta.chatty.dal.PreparedStatementExecuter;
import edu.mta.chatty.dal.handlers.BatchHandler;

public class DBHelper {
	final private PreparedStatementExecuter executer;
	public DBHelper(DataSource dataSource){
		this.executer = new PreparedStatementExecuter(dataSource);
	}
	
	
	public void executeBatchFromIS(InputStream is) throws IOException, SQLException{
		final String statements = IOHelper.readAsString(is);
		executer.execute(new BatchHandler() {
			@Override
			public List<String> getBatchList() {
				String[] statementsArr = statements.split(";");
				List<String> statementList = new LinkedList<String>();
				for (String statement:statementsArr){
					String trimmed = statement.trim();
					if (trimmed.equals("") == false){
						statementList.add(trimmed);
					}
				}
				return statementList;
			}
			
			@Override
			public void handleResults(int[] result) {
				//TODO: do we need handle results?
			}
		});
	}
	
}
