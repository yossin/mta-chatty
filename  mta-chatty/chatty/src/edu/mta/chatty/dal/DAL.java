package edu.mta.chatty.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import edu.mta.chatty.contract.LoginRequest;
import edu.mta.chatty.dal.handlers.ListQueryHandler;
import edu.mta.chatty.domain.User;

public class DAL {
	private final static Logger logger = Logger.getLogger(DAL.class.getName());
	private PreparedStatementExecuter executer;
	final public Users users = new Users();
	
	public class Users{
		public User login(final LoginRequest request) throws SQLException{
			final List<User> results = new LinkedList<User>();
			ListQueryHandler<User> handler = new ListQueryHandler<User>(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, request.getEmail());
					statement.setString(2, request.getPassword());
				}
				@Override
				public String getSql() {
					return "select u.email, u.name, u.picture from `user` as u where u.email=? and u.password=?";
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
				logger.fine(String.format("user %s has been logged in successfully", request.getEmail()));
				return results.get(0);
			} else {
				logger.warning(String.format("unable to login user %s. might be a wrong user or password", request.getEmail()));
				return null;
			}
		}
		
	}
	
	public DAL(DataSource ds){
		executer=new PreparedStatementExecuter(ds);
	}
}
