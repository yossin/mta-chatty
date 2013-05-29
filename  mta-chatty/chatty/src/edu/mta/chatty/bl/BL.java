package edu.mta.chatty.bl;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import edu.mta.chatty.contract.GenericDataResponse;
import edu.mta.chatty.contract.LoginRequest;
import edu.mta.chatty.contract.UserDataResponse;
import edu.mta.chatty.contract.UserRequest;
import edu.mta.chatty.dal.DAL;
import edu.mta.chatty.domain.BuddyList;
import edu.mta.chatty.domain.BuddyMessages;
import edu.mta.chatty.domain.Group;
import edu.mta.chatty.domain.GroupMemberships;
import edu.mta.chatty.domain.GroupMessages;
import edu.mta.chatty.domain.SearchRequest;
import edu.mta.chatty.domain.User;
import edu.mta.chatty.domain.UserData;

public class BL {
	private final static Logger logger = Logger.getLogger(BL.class.getName());
	private DAL dal;
	final public Users users = new Users();
	final public Groups groups = new Groups();
	final public Data data = new Data();

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

		public List<User> find(SearchRequest request) throws IllegalArgumentException, Exception {
			BLExecuter<SearchRequest, List<User>> executer = new BLExecuter<SearchRequest, List<User>>();

			List<User> users = executer.execute(new BLRequest<SearchRequest, List<User>>() {
				@Override
				public void validate(SearchRequest t) throws IllegalArgumentException {
					Validator.validateEmail(t.getEmail(), "email");
					Validator.validateNotEmpty(t.getText(), "text");
				}
				
				@Override
				public List<User> perform(SearchRequest t) throws Exception {
					try {
						return dal.users.find(t);
					} catch (SQLException e) {
						String msg = String.format("unable to find users by text %s, with error %s", t, e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
				}
			}, request);
			return users;
		}
	}
	
	public class Groups{


		public List<Group> find(SearchRequest request) throws IllegalArgumentException, Exception {
			BLExecuter<SearchRequest, List<Group>> executer = new BLExecuter<SearchRequest, List<Group>>();

			List<Group> groups = executer.execute(new BLRequest<SearchRequest, List<Group>>() {
				@Override
				public void validate(SearchRequest t) throws IllegalArgumentException {
					Validator.validateEmail(t.getEmail(), "email");
					Validator.validateNotEmpty(t.getText(), "text");
				}
				
				@Override
				public List<Group> perform(SearchRequest t) throws Exception {
					try {
						return dal.groups.find(t);
					} catch (SQLException e) {
						String msg = String.format("unable to find groups by text %s, with error %s", t, e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
				}
			}, request);
			return groups;
		}
	}
	
	public class Data{
		public GenericDataResponse getGenericData() throws IllegalArgumentException, Exception{
			BLExecuter<Void, GenericDataResponse> executer = new BLExecuter<Void, GenericDataResponse>();
			GenericDataResponse data = executer.execute(new BLRequest<Void, GenericDataResponse>() {
				@Override
				public void validate(Void t) throws IllegalArgumentException {
				}
				
				@Override
				public GenericDataResponse perform(Void t) throws Exception {
					try {
						return dal.data.getGenericData();
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
		public UserDataResponse getUserData(UserRequest request) throws IllegalArgumentException, Exception{
			BLExecuter<UserRequest, UserDataResponse> executer = new BLExecuter<UserRequest, UserDataResponse>();
			UserDataResponse data = executer.execute(new BLRequest<UserRequest, UserDataResponse>() {
				@Override
				public void validate(UserRequest t) throws IllegalArgumentException {
					Validator.validateEmail(t.getEmail(), "email");
				}
				
				@Override
				public UserDataResponse perform(UserRequest t) throws Exception {
					try {
						String ownerEmail = t.getEmail();
						UserData userData = new UserData();
						List<User> users= dal.data.getBuddies(ownerEmail);
						List<Group> groups = dal.data.getUserGroups(ownerEmail);
						List<BuddyList> buddyList = dal.data.getBuddyList(ownerEmail);
						List<GroupMemberships> groupMemberships = dal.data.getGroupList(ownerEmail);
						List<BuddyMessages> buddyMessages = dal.data.getBuddiesMessages(ownerEmail);
						List<GroupMessages> groupMessages = dal.data.getGroupsMessages(ownerEmail);
						userData.setUsers(users);
						userData.setGroups(groups);
						userData.setBuddy_list(buddyList);
						userData.setGroup_memberships(groupMemberships);
						userData.setBuddy_messages(buddyMessages);
						userData.setGroup_messages(groupMessages);
						return userData;
					} catch (SQLException e) {
						String msg = String.format("unable to get generic data, with error %s", e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
				}
			}, request);
			return data;
		}
		public UserData getCompleteUserData(UserRequest request) throws IllegalArgumentException, Exception{
			UserData data = (UserData) getUserData(request);
			GenericDataResponse generic = getGenericData();
			data.setCities(generic.getCities());
			data.setCountries(generic.getCountries());
			return data;
		}		
	}
	
	
	

}
