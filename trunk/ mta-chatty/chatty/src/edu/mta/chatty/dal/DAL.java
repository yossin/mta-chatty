package edu.mta.chatty.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import edu.mta.chatty.contract.GenericDataResponse;
import edu.mta.chatty.contract.LoginRequest;
import edu.mta.chatty.dal.handlers.ListQueryHandler;
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
	
	public class GenericData{
		private List<Country> getCountries() throws SQLException{
			final List<Country> results = new LinkedList<Country>();
			ListQueryHandler<Country> handler = new ListQueryHandler<Country>(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					//no parameters
				}
				@Override
				public String getSql() {
					return "select country_id,country,last_update from country";
				}
				@Override
				protected Country handleResult(ResultSet resultSet) throws SQLException{
					Country country = new Country();
					country.setCountry_id(resultSet.getInt(1));
					country.setCountry(resultSet.getString(2));
					country.setLast_update(resultSet.getTimestamp(3));
					return country;
				}
			};
			executer.execute(handler);
			return results;
		}
		
		private List<City> getCities() throws SQLException{
			final List<City> results = new LinkedList<City>();
			ListQueryHandler<City> handler = new ListQueryHandler<City>(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					//no parameters
				}
				@Override
				public String getSql() {
					return "select city_id,city,country_id,last_update from city";
				}
				@Override
				protected City handleResult(ResultSet resultSet) throws SQLException{
					City city = new City();
					city.setCity_id(resultSet.getInt(1));
					city.setCity(resultSet.getString(2));
					city.setCountry_id(resultSet.getInt(3));
					city.setLast_update(resultSet.getTimestamp(4));
					return city;
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
