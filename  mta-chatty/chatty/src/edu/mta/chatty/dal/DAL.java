package edu.mta.chatty.dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import edu.mta.chatty.contract.GenericDataResponse;
import edu.mta.chatty.contract.LoginRequest;
import edu.mta.chatty.dal.handlers.GenericListQueryHandler;
import edu.mta.chatty.domain.City;
import edu.mta.chatty.domain.Country;
import edu.mta.chatty.domain.User;
import edu.mta.chatty.domain.UserData;

public class DAL {
	private final static Logger logger = Logger.getLogger(DAL.class.getName());
	private PreparedStatementExecuter executer;
	final public Users users = new Users();
	final public GenericData generic = new GenericData();
	
	public class Users{
		private abstract class UserListQueryHandler extends GenericListQueryHandler<User>{
			UserListQueryHandler(List<User> results){
				super(results);
			}
			@Override
			protected User create() {
				return new User();
			}
		}
		
		public User login(final LoginRequest request) throws SQLException{
			final List<User> results = new LinkedList<User>();
			UserListQueryHandler handler = new UserListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, request.getEmail());
					statement.setString(2, request.getPassword());
				}
				@Override
				public String getSql() {
					return "select u.email, u.name, u.picture from `user` as u where u.email=? and u.password=?";
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
		
		public List<User> getBuddies(final String ownerEmail) throws SQLException{
			final List<User> results = new LinkedList<User>();
			UserListQueryHandler handler = new UserListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
					statement.setString(2, ownerEmail);
					statement.setString(3, ownerEmail);
				}
				@Override
				public String getSql() {
					return "select u.email,u.name,u.picture,u.active,u.last_update from `user` as u join (select g.member_email from group_membership as g join (select group_id from group_membership where member_email=?) as z on g.group_id=z.group_id where g.member_email!=? union select u.email from `user` as u join buddy_list as b on u.email=b.buddy_id where b.owner_email=?) as z on u.email=z.member_email";
				}
			};
			
			executer.execute(handler);
			return results;
		}
		
	}
	
	public class GenericData{
		private abstract class CityListQueryHandler extends GenericListQueryHandler<City>{
			CityListQueryHandler(List<City> results){
				super(results);
			}
			@Override
			protected City create() {
				return new City();
			}
		}
		private abstract class CountryListQueryHandler extends GenericListQueryHandler<Country>{
			CountryListQueryHandler(List<Country> results){
				super(results);
			}
			@Override
			protected Country create() {
				return new Country();
			}
		}
		private List<Country> getCountries() throws SQLException{
			final List<Country> results = new LinkedList<Country>();
			CountryListQueryHandler handler = new CountryListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					//no parameters
				}
				@Override
				public String getSql() {
					return "select country_id,country,last_update from country";
				}
			};
			executer.execute(handler);
			return results;
		}
		
		private List<City> getCities() throws SQLException{
			final List<City> results = new LinkedList<City>();
			CityListQueryHandler handler = new CityListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					//no parameters
				}
				@Override
				public String getSql() {
					return "select city_id,city,country_id,last_update from city";
				}
			};
			executer.execute(handler);
			return results;
		}
		
		public GenericDataResponse getGenericData() throws SQLException{
			UserData results = new UserData();
			results.setCountries(getCountries());
			results.setCities(getCities());
			return results;
		}

	}
	
	
	public DAL(DataSource ds){
		executer=new PreparedStatementExecuter(ds);
	}
}
