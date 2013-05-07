package edu.mta.chatty.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import edu.mta.chatty.dal.handlers.ListQueryHandler;
import edu.mta.chatty.domain.User;

public class DAL {
	private final static Logger logger = Logger.getLogger(DAL.class.getName());
	private PreparedStatementExecuter executer;
	
	public DAL(DataSource ds){
		executer=new PreparedStatementExecuter(ds);
	}
	public User login(final String user, final String password) throws SQLException{
		final List<User> results = new LinkedList<User>();
		ListQueryHandler<User> handler = new ListQueryHandler<User>(results) {
			@Override
			public void setVariables(PreparedStatement statement) throws SQLException {
				statement.setString(1, user);
				statement.setString(2, password);
			}
			@Override
			public String getSql() {
				return "select u.email, u.name, u.picture from 'user' as u where u.email==? and u.password==?";
			}
			@Override
			protected User handleResult(ResultSet resultSet) throws SQLException{
				User user = new User();
				user.setEmail(resultSet.getString(1));
				user.setName(resultSet.getString(2));
				user.setPicture(resultSet.getString(3));
				return user;
			}
		};
		
		executer.execute(handler);
		
		if (results.size()>0){
			logger.fine(String.format("user %s has been logged in successfully", user));
			return results.get(0);
		} else {
			logger.warning(String.format("unable to login user %s. might be a wrong user or password", user));
			return null;
		}
	}
}
