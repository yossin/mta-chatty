package edu.mta.chatty.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import edu.mta.chatty.dal.handlers.BatchHandler;
import edu.mta.chatty.dal.handlers.Handler;
import edu.mta.chatty.dal.handlers.InsertHandler;
import edu.mta.chatty.dal.handlers.QueryHandler;
import edu.mta.chatty.dal.handlers.SafeBatchInsert;
import edu.mta.chatty.dal.handlers.UpdateHandler;

public class PreparedStatementExecuter {
	private static Logger logger = Logger.getLogger(PreparedStatementExecuter.class.getName());
	private SimpleConManager db;
	
	static interface Command{
		PreparedStatement prepareStatement(Connection connection) throws SQLException;
		void execute(PreparedStatement statement) throws SQLException;
	}
	interface Executer{
		void execute(PreparedStatement statement) throws SQLException;
	}
	
	static abstract class SinglePreparedCommand extends CommandExecuter{
		Handler handler;
		SinglePreparedCommand(Handler handler,Executer executer){
			super(executer);
			this.handler=handler;
		}
		@Override
		public PreparedStatement prepareStatement(Connection connection) throws SQLException {
			PreparedStatement statement  = connection.prepareStatement(handler.getSql(),Statement.RETURN_GENERATED_KEYS);
			handler.setVariables(statement);
			return statement;
		}
	}
	static abstract class CommandExecuter implements Command{
		Executer executer;
		CommandExecuter(Executer executer){
			this.executer=executer;
		}
		@Override
		public void execute(PreparedStatement statement) throws SQLException {
			executer.execute(statement);
		}
	}
	
	
	
	public PreparedStatementExecuter(DataSource dataSource){
		db = new SimpleConManager(dataSource);
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

	private static void close(ResultSet results){
		if (results!=null){
			try {
				results.close();
			} catch (SQLException e) {
				logger.warning("unable to close result set"+e);
			}
		}
	}


	
	private void execute(Handler handler, Executer executer) throws SQLException{
		SinglePreparedCommand command = new SinglePreparedCommand(handler,executer){};
		execute(command);
	}
	
	private void execute(Command command) throws SQLException{
		Connection connection = db.connect();
		PreparedStatement statement = null;
		try {
			statement = command.prepareStatement(connection);
			command.execute(statement);
		} catch (SQLException e){
			logger.severe(String.format("error while executing statement  %s, exception %s",statement, e));
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally{
			close(statement);
			db.disconnect();
		} 
	}

	public void execute(final QueryHandler handler) throws SQLException{
		execute(handler, new Executer() {
			@Override
			public void execute(PreparedStatement statement)
					throws SQLException {
				ResultSet results = statement.executeQuery();
				try {
					handler.handleResults(results);
				} finally{
					close(results);
				}
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

	public void execute(final InsertHandler handler) throws SQLException{
		execute(handler, new Executer() {
			@Override
			public void execute(PreparedStatement statement)
					throws SQLException {
				int affectedRows = statement.executeUpdate();
				if (affectedRows<1){
		            throw new SQLException("Insert statement failed, no rows affected.");
				}
				ResultSet generatedKeys = statement.getGeneratedKeys();
				try {
					handler.handleResults(generatedKeys);
				} finally{
					close(generatedKeys);
				}
			}
		});
	}

	
	public <T>void execute(final SafeBatchInsert<T> handler) throws SQLException{
		Executer executer = new Executer(){
			@Override
			public void execute(PreparedStatement statement)
					throws SQLException {
				handler.handleResult(statement.executeBatch());
			}
		};
		CommandExecuter command = new CommandExecuter(executer) {
			@Override
			public PreparedStatement prepareStatement(Connection connection)
					throws SQLException {
				PreparedStatement statement  = connection.prepareStatement(handler.getSql());
				for(T t: handler.getInsertData()){
					handler.setVariables(statement,t);
					statement.addBatch();
				}
				return statement;
			}
		};
		execute(command);
	}

	
	
	
	
	public void execute(final BatchHandler handler) throws SQLException{
		Connection connection = db.connect();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			for (String batch : handler.getBatchList()){
				statement.addBatch(batch);
			}
			handler.handleResults(statement.executeBatch());
		} catch (SQLException e){
			logger.severe(String.format("error while executing sqls %s, exception is %s",handler.getBatchList(), e));
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		} finally{
			close(statement);
			db.disconnect();
		} 
	}

	
	
}
