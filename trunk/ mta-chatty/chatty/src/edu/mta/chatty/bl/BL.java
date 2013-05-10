package edu.mta.chatty.bl;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import edu.mta.chatty.contract.GenericDataResponse;
import edu.mta.chatty.contract.LoginRequest;
import edu.mta.chatty.dal.DAL;
import edu.mta.chatty.domain.User;

public class BL {
	private final static Logger logger = Logger.getLogger(BL.class.getName());
	private DAL dal;
	final public Users users = new Users();
	final public GenericData generic = new GenericData();

	public BL(DataSource ds){
		dal = new DAL(ds);
	}
	public class Users{

		public User login(LoginRequest request) throws IllegalArgumentException, Exception{
			BLExecuter<LoginRequest, User> executer = new BLExecuter<LoginRequest, User>();
			User user = executer.execute(new BLRequest<LoginRequest, User>() {
				@Override
				public void validate(LoginRequest t) throws IllegalArgumentException {
					Validator.validateEmail(t.getEmail(), "email");
					Validator.validateNotEmpty(t.getPassword(), "password");
				}
				
				@Override
				public User perform(LoginRequest t) throws Exception {
					try {
						User user = dal.users.login(t);
						if (user == null){
							logger.warning(String.format("unable to login user %s", t.getEmail()));
						}
						return user;
					} catch (SQLException e) {
						String msg = String.format("unable to login user %s, with error %s", t.getEmail(), e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
				}
			}, request);
			return user;
		}
	}
	
	public class GenericData{
		public GenericDataResponse getGenericData() throws IllegalArgumentException, Exception{
			BLExecuter<Void, GenericDataResponse> executer = new BLExecuter<Void, GenericDataResponse>();
			GenericDataResponse data = executer.execute(new BLRequest<Void, GenericDataResponse>() {
				@Override
				public void validate(Void t) throws IllegalArgumentException {
				}
				
				@Override
				public GenericDataResponse perform(Void t) throws Exception {
					try {
						return dal.generic.getGenericData();
					} catch (SQLException e) {
						String msg = String.format("unable to get generic data, with error %s", e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
				}
			}, null);
			return data;
		}
	}
	
	
	

}
