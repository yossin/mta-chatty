package edu.mta.chatty.bl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import edu.mta.chatty.contract.GenericDataResponse;
import edu.mta.chatty.contract.LoginRequest;
import edu.mta.chatty.dal.DAL;
import edu.mta.chatty.domain.BuddyList;
import edu.mta.chatty.domain.BuddyMessages;
import edu.mta.chatty.domain.CreateGroupRequest;
import edu.mta.chatty.domain.Group;
import edu.mta.chatty.domain.GroupMemberships;
import edu.mta.chatty.domain.GroupMessages;
import edu.mta.chatty.domain.SearchRequest;
import edu.mta.chatty.domain.SyncUserRequest;
import edu.mta.chatty.domain.User;
import edu.mta.chatty.domain.UserData;
import edu.mta.chatty.domain.admin.CountStatistics;
import edu.mta.chatty.domain.admin.DateRangeRequest;

public class BL {
	private final static Logger logger = Logger.getLogger(BL.class.getName());
	private DAL dal;
	final public Users users = new Users();
	final public Groups groups = new Groups();
	final public Data data = new Data();
	final public Messages messages = new Messages();
	final public Admin admin = new Admin();

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
		
		public void updateImage(final String userId, final String image) throws IllegalArgumentException, Exception {
			BLExecuter<Void, Void> executer = new BLExecuter<Void, Void>();

			executer.execute(new BLRequest<Void, Void>() {
				@Override
				public void validate(Void t) throws IllegalArgumentException {
					Validator.validateEmail(userId, "buddy");
					Validator.validateNotEmpty(image, "image");
				}
				
				@Override
				public Void perform(Void t) throws Exception {
					try {
						dal.users.updateUserImage(userId, image);
						return null;
					} catch (SQLException e) {
						String msg = String.format("unable to update image %s for %s.error %s", 
								image, userId, e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
				}
			}, null);
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
		
		public void create(CreateGroupRequest request) throws IllegalArgumentException, Exception {
			BLExecuter<CreateGroupRequest, Void> executer = new BLExecuter<CreateGroupRequest, Void>();

			executer.execute(new BLRequest<CreateGroupRequest, Void>() {
				@Override
				public void validate(CreateGroupRequest t) throws IllegalArgumentException {
					GroupMemberships memberships = t.getMemberships();
					Group group = t.getGroup();
					Validator.validateEmail(memberships.getMember_email(), "member_email");
					Validator.validateNotEmpty(group.getName(), "group name");
					Validator.validateNotEmpty(group.getDescription(), "group description");
					Validator.validateNotEmpty(group.getPicture(), "group picture");
				}
				
				private void createGroup(CreateGroupRequest t) throws Exception{
					Group group = t.getGroup();
					try {
						dal.groups.create(group);
						t.getMemberships().setGroup_id(group.getGroup_id());
					} catch (SQLException e) {
						String msg = String.format("unable to create group name=%d description=%s, with error %s", group.getName(), group.getDescription(), e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
				}
				
				@Override
				public Void perform(CreateGroupRequest t) throws Exception {
					createGroup(t);
					joinInto(t.getMemberships());
					return null;
				}
			}, request);
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
						return dal.data.getGenericData(new Timestamp(0), new Timestamp(System.currentTimeMillis()));
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
		public GenericDataResponse getGenericData(final SyncUserRequest request) throws IllegalArgumentException, Exception{
			BLExecuter<Void, GenericDataResponse> executer = new BLExecuter<Void, GenericDataResponse>();
			GenericDataResponse data = executer.execute(new BLRequest<Void, GenericDataResponse>() {
				@Override
				public void validate(Void t) throws IllegalArgumentException {
				}
				
				@Override
				public GenericDataResponse perform(Void t) throws Exception {
					try {
						return dal.data.getGenericData(request.getBeginTS(), request.getEndTS());
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
		public UserData getUserData(SyncUserRequest request) throws IllegalArgumentException, Exception{
			BLExecuter<SyncUserRequest, UserData> executer = new BLExecuter<SyncUserRequest, UserData>();
			UserData data = executer.execute(new BLRequest<SyncUserRequest, UserData>() {
				@Override
				public void validate(SyncUserRequest t) throws IllegalArgumentException {
					Validator.validateEmail(t.getEmail(), "email");
				}
				
				@Override
				public UserData perform(SyncUserRequest t) throws Exception {
					try {
						String ownerEmail = t.getEmail();
						Timestamp begin = t.getBeginTS();
						Timestamp end = t.getEndTS();
						
						UserData userData = new UserData();
						List<User> users= dal.data.getUsers(ownerEmail, begin, end);
						List<Group> groups = dal.data.getUserGroups(ownerEmail, begin, end);
						List<BuddyList> buddyList = dal.data.getBuddyList(ownerEmail, begin, end);
						List<GroupMemberships> groupMemberships = dal.data.getGroupList(ownerEmail, begin, end);
						List<BuddyMessages> buddyMessages = dal.data.getBuddiesMessages(ownerEmail, begin, end);
						List<GroupMessages> groupMessages = dal.data.getGroupsMessages(ownerEmail, begin, end);
						userData.setUsers(users);
						userData.setGroups(groups);
						userData.setBuddy_list(buddyList);
						userData.setGroup_memberships(groupMemberships);
						userData.setBuddy_messages(buddyMessages);
						userData.setGroup_messages(groupMessages);
						userData.setSyncSpan(t.getSyncSpan());
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
		public UserData getCompleteUserData(SyncUserRequest request) throws IllegalArgumentException, Exception{
			UserData data = (UserData) getUserData(request);
			GenericDataResponse generic = getGenericData(request);
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
	
	public class Admin{
		public CountStatistics getCountStatistics(DateRangeRequest request) throws IllegalArgumentException, Exception{
			BLExecuter<DateRangeRequest, CountStatistics> executer = new BLExecuter<DateRangeRequest, CountStatistics>();
			CountStatistics data = executer.execute(new BLRequest<DateRangeRequest, CountStatistics>() {
				@Override
				public void validate(DateRangeRequest t) throws IllegalArgumentException {
					//Validator.validateEmail(t.getEmail(), "email");
				}
				
				@Override
				public CountStatistics perform(DateRangeRequest t) throws Exception {
					try {
						CountStatistics stat = dal.admin.getCountStatistics(t);
						return stat;
					} catch (SQLException e) {
						String msg = String.format("unable to get statistics, with error %s", e);
						logger.severe(msg);
						logger.log(Level.SEVERE, e.getMessage(), e);
						throw new Exception (msg, e);
					}
				}
			}, request);
			return data;
		}
	}


}
