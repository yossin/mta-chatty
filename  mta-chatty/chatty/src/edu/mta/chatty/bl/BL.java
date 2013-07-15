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
	final public Messages messages = new Messages();

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
		
		public void addBuddy(BuddyList request) throws IllegalArgumentException, Exception {
			BLExecuter<BuddyList, Void> executer = new BLExecuter<BuddyList, Void>();

			executer.execute(new BLRequest<BuddyList, Void>() {
				@Override
				public void validate(BuddyList t) throws IllegalArgumentException {
					Validator.validateEmail(t.getOwner_email(), "owner email");
					Validator.validateEmail(t.getBuddy_id(), "buddy id");
				}
				
				@Override
				public Void perform(BuddyList t) throws Exception {
					try {
						dal.users.addBuddy(t);
						return null;
					} catch (SQLException e) {
						String msg = String.format("unable to add buddyId=%s for memberId=%s, with error %s", t.getBuddy_id(), t.getOwner_email(), e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
				}
			}, request);
		}
		
		public void register(User request) throws IllegalArgumentException, Exception {
			BLExecuter<User, Void> executer = new BLExecuter<User, Void>();

			executer.execute(new BLRequest<User, Void>() {
				@Override
				public void validate(User t) throws IllegalArgumentException {
					Validator.validateEmail(t.getEmail(), "email");
					Validator.validateNotEmpty(t.getName(), "name");
					Validator.validateNotEmpty(t.getPicture(), "picture");
					Validator.validateNotEmpty(t.getPassword(), "password");
				}
				
				@Override
				public Void perform(User t) throws Exception {
					try {
						dal.users.insertUser(t);
						return null;
					} catch (SQLException e) {
						String msg = String.format("unable to register new user:name=%s, email=%s, picture=%s, password=%s .error %s", 
								t.getName(), t.getEmail(), t.getPicture(), t.getPassword(), e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
				}
			}, request);
		}
		//TODO: remove buddy

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
		
		public void joinInto(GroupMemberships request) throws IllegalArgumentException, Exception {
			BLExecuter<GroupMemberships, Void> executer = new BLExecuter<GroupMemberships, Void>();

			executer.execute(new BLRequest<GroupMemberships, Void>() {
				@Override
				public void validate(GroupMemberships t) throws IllegalArgumentException {
					Validator.validateEmail(t.getMember_email(), "member_email");
				}
				
				@Override
				public Void perform(GroupMemberships t) throws Exception {
					try {
						dal.groups.joinInto(t);
					} catch (SQLException e) {
						String msg = String.format("unable to join into groupId=%d & memberId=%s, with error %s", t.getGroup_id(), t.getMember_email(), e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
					return null;
				}
			}, request);
		}
		
		public void leave(GroupMemberships request) throws IllegalArgumentException, Exception {
			BLExecuter<GroupMemberships, Void> executer = new BLExecuter<GroupMemberships, Void>();

			executer.execute(new BLRequest<GroupMemberships, Void>() {
				@Override
				public void validate(GroupMemberships t) throws IllegalArgumentException {
					Validator.validateEmail(t.getMember_email(), "member_email");
				}
				
				@Override
				public Void perform(GroupMemberships t) throws Exception {
					try {
						dal.groups.leave(t);
					} catch (SQLException e) {
						String msg = String.format("unable to leave groupId=%d & memberId=%s, with error %s", t.getGroup_id(), t.getMember_email(), e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
					return null;
				}
			}, request);
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
	
	public class Messages{
		public void send(GroupMessages request) throws IllegalArgumentException, Exception {
			BLExecuter<GroupMessages, Void> executer = new BLExecuter<GroupMessages, Void>();

			executer.execute(new BLRequest<GroupMessages, Void>() {
				@Override
				public void validate(GroupMessages t) throws IllegalArgumentException {
					Validator.validateEmail(t.getSender_id(), "sender id");
					Validator.validateNotEmpty(t.getMessage(), "message");
				}
				
				@Override
				public Void perform(GroupMessages t) throws Exception {
					try {
						dal.messages.send(t);
					} catch (SQLException e) {
						String msg = String.format("unable to send group-message %s from %s into groupId %d. error %s", t.getMessage(), t.getSender_id(), t.getReceiver_id(),e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
					return null;
				}
			}, request);
		}
		
		public void send(BuddyMessages request) throws IllegalArgumentException, Exception {
			BLExecuter<BuddyMessages, Void> executer = new BLExecuter<BuddyMessages, Void>();

			executer.execute(new BLRequest<BuddyMessages, Void>() {
				@Override
				public void validate(BuddyMessages t) throws IllegalArgumentException {
					Validator.validateEmail(t.getSender_id(), "sender id");
					Validator.validateEmail(t.getReceiver_id(), "receiver id");
					Validator.validateNotEmpty(t.getMessage(), "message");
				}
				
				@Override
				public Void perform(BuddyMessages t) throws Exception {
					try {
						dal.messages.send(t);
					} catch (SQLException e) {
						String msg = String.format("unable to send buddy-message %s from %s into %s. error %s", t.getMessage(), t.getSender_id(), t.getReceiver_id(),e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
					return null;
				}
			}, request);
		}
		
	}
	

}
