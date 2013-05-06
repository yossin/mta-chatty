package edu.mta.chatty.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.DataSource;

import edu.mta.chatty.dal.handlers.BatchHandler;
import edu.mta.chatty.dal.handlers.Handler;
import edu.mta.chatty.dal.handlers.QueryHandler;
import edu.mta.chatty.dal.handlers.SafeBatchInsert;
import edu.mta.chatty.dal.handlers.UpdateHandler;

public class PreparedStatementExecuter {
	private static Logger logger = Logger.getLogger(PreparedStatementExecuter.class.getName());
	private SimpleConManager dal;
	
	public PreparedStatementExecuter(DataSource dataSource){
		dal = new SimpleConManager(dataSource);
	}
	
	private static void close(Statement statement){
		if (statement!=null){
			try {
				statement.close();
			} catch (SQLException e) {
				logger.warning("unable to close prepared statement "+e);
			}
		}
	}
	private PreparedStatement prepareStatement(Connection connection, Handler handler) throws SQLException{
		PreparedStatement statement  = connection.prepareStatement(handler.getSql());
		handler.setVariables(statement);
		return statement;
	}
	private interface Executer{
		void execute(PreparedStatement statement) throws SQLException;
	}
	
	private void execute(Handler handler, Executer executer) throws SQLException{
		Connection connection = dal.connect();
		PreparedStatement statement = null;
		try {
			statement = prepareStatement(connection, handler);
			executer.execute(statement);
		} catch (SQLException e){
			logger.severe(String.format("error while executing sql %s, exception %s",handler.getSql(), e));
		} finally{
			close(statement);
			dal.disconnect();
		} 
	}

	public void execute(final QueryHandler handler) throws SQLException{
		execute(handler, new Executer() {
			@Override
			public void execute(PreparedStatement statement)
					throws SQLException {
				handler.handleResults(statement.executeQuery());
			}
		});
	}
	public void execute(final UpdateHandler handler) throws SQLException{
		execute(handler, new Executer() {
			@Override
			public void execute(PreparedStatement statement)
					throws SQLException {
				handler.handleResults(statement.executeUpdate());
			}
		});
	}
	public void execute(final BatchHandler handler) throws SQLException{
		Connection connection = dal.connect();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			for (String batch : handler.getBatchList()){
				statement.addBatch(batch);
			}
			handler.handleResults(statement.executeBatch());
		} catch (SQLException e){
			logger.severe(String.format("error while executing sqls %s, exception is %s",handler.getBatchList(), e));
			throw e;
		} finally{
			close(statement);
			dal.disconnect();
		} 
	}
	private <T> PreparedStatement prepareStatement(Connection connection, SafeBatchInsert<T> handler) throws SQLException{
		PreparedStatement statement  = connection.prepareStatement(handler.getSql());
		for(T t: handler.getInsertData()){
			handler.setVariables(statement,t);
			statement.addBatch();
		}
		return statement;
	}

	public <T>void execute(final SafeBatchInsert<T> handler) throws SQLException{
		Connection connection = dal.connect();
		PreparedStatement statement = null;
		try {
			statement = prepareStatement(connection, handler);
			handler.handleResult(statement.executeBatch());
		} catch (SQLException e){
			logger.severe(String.format("error while executing batch sql %s, exception %s",handler.getSql(), e));
		} finally{
			close(statement);
			dal.disconnect();
		} 	
	}

}
