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
import edu.mta.chatty.dal.handlers.UpdateHandler;
import edu.mta.chatty.domain.BuddyList;
import edu.mta.chatty.domain.BuddyMessages;
import edu.mta.chatty.domain.City;
import edu.mta.chatty.domain.Country;
import edu.mta.chatty.domain.Group;
import edu.mta.chatty.domain.GroupMemberships;
import edu.mta.chatty.domain.GroupMessages;
import edu.mta.chatty.domain.SearchRequest;
import edu.mta.chatty.domain.User;
import edu.mta.chatty.domain.UserData;

public class DAL {
	private final static Logger logger = Logger.getLogger(DAL.class.getName());
	private PreparedStatementExecuter executer;
	final public Users users = new Users();
	final public Data data = new Data();
	final public Groups groups = new Groups();
	
	private static abstract class UserListQueryHandler extends GenericListQueryHandler<User>{
		UserListQueryHandler(List<User> results){
			super(results);
		}
		@Override
		protected User create() {
			return new User();
		}
	}
	private static abstract class BuddyListListQueryHandler extends GenericListQueryHandler<BuddyList>{
		BuddyListListQueryHandler(List<BuddyList> results){
			super(results);
		}
		@Override
		protected BuddyList create() {
			return new BuddyList();
		}
	}
	private static abstract class GroupMembershipsListQueryHandler extends GenericListQueryHandler<GroupMemberships>{
		GroupMembershipsListQueryHandler(List<GroupMemberships> results){
			super(results);
		}
		@Override
		protected GroupMemberships create() {
			return new GroupMemberships();
		}
	}
	private static abstract class BuddyMessagesListQueryHandler extends GenericListQueryHandler<BuddyMessages>{
		BuddyMessagesListQueryHandler(List<BuddyMessages> results){
			super(results);
		}
		@Override
		protected BuddyMessages create() {
			return new BuddyMessages();
		}
	}
	private static abstract class GroupMessagesListQueryHandler extends GenericListQueryHandler<GroupMessages>{
		GroupMessagesListQueryHandler(List<GroupMessages> results){
			super(results);
		}
		@Override
		protected GroupMessages create() {
			return new GroupMessages();
		}
	}

	public class Users{
		
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

		public List<User> find(final SearchRequest request) throws SQLException{
			final List<User> results = new LinkedList<User>();
			UserListQueryHandler handler = new UserListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, '%'+request.getText()+'%');
					statement.setString(2, '%'+request.getText()+'%');
					statement.setString(3, request.getEmail());
				}
				@Override
				public String getSql() {
					return "select u.email, u.name, u.picture from `user` as u left join buddy_list as bl on u.email=bl.buddy_id where (bl.buddy_id is null) and (u.name like ? or u.email like ?) and u.email!=?";
				}
			};
			
			executer.execute(handler);
			
			return results;
		}
		
		public void addBuddy(final BuddyList request) throws SQLException{
			UpdateHandler handler = new UpdateHandler() {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1,request.getOwner_email());
					statement.setString(2,request.getBuddy_id());
				}
				@Override
				public String getSql() {
					return "insert into buddy_list (owner_email, buddy_id) values(?,?)";
				}
				
				@Override
				public void handleResults(int result) {
				}
			};
			executer.execute(handler);
		}
		
		public void insertUser(final User request) throws SQLException{
			UpdateHandler handler = new UpdateHandler() {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1,request.getEmail());
					statement.setString(2,request.getName());
					statement.setString(3,request.getPicture());
					statement.setString(4,request.getPassword());
				}
				@Override
				public String getSql() {
					return "insert into `user` (email, name, picture, password) values(?,?,?,?)";
				}
				
				@Override
				public void handleResults(int result) {
				}
			};
			executer.execute(handler);
		}
		
	}
	
	private static abstract class GroupListQueryHandler extends GenericListQueryHandler<Group>{
		GroupListQueryHandler(List<Group> results){
			super(results);
		}
		@Override
		protected Group create() {
			return new Group();
		}
	}
	public class Groups{
		public List<Group> find(final SearchRequest request) throws SQLException{
			final List<Group> results = new LinkedList<Group>();
			GroupListQueryHandler handler = new GroupListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, '%'+request.getText()+'%');
					statement.setString(2, request.getEmail());
				}
				@Override
				public String getSql() {
					return "select g.group_id, g.name, g.picture, g.description from `group` as g  where g.name like ? and g.group_id not in (select gm.group_id from group_membership as gm where gm.member_email=?)";
				}
			};
			
			executer.execute(handler);
			
			return results;
		}

		public void joinInto(final GroupMemberships memberships) throws SQLException{
			UpdateHandler handler = new UpdateHandler() {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1,memberships.getMember_email());
					statement.setInt(2,memberships.getGroup_id());
				}
				@Override
				public String getSql() {
					return "insert into group_membership (member_email, group_id) values (?,?)";
				}
				
				@Override
				public void handleResults(int result) {
				}
			};
			executer.execute(handler);
		}
	
		
		public void leave(final GroupMemberships memberships) throws SQLException{
			UpdateHandler handler = new UpdateHandler() {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1,memberships.getMember_email());
					statement.setInt(2,memberships.getGroup_id());
				}
				@Override
				public String getSql() {
					return "delete from group_membership where member_email=? and group_id=?";
				}
				
				@Override
				public void handleResults(int result) {
				}
			};
			executer.execute(handler);
		}

		
	}
	
	
	
	public class Data{
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
		
		public List<BuddyList> getBuddyList(final String ownerEmail) throws SQLException{
			final List<BuddyList> results = new LinkedList<BuddyList>();
			BuddyListListQueryHandler handler = new BuddyListListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
				}
				@Override
				public String getSql() {
					return "select owner_email,buddy_id,last_update from buddy_list where owner_email=?";
				}
			};
			executer.execute(handler);
			return results;
		}

		public List<GroupMemberships> getGroupList(final String ownerEmail) throws SQLException{
			final List<GroupMemberships> results = new LinkedList<GroupMemberships>();
			GroupMembershipsListQueryHandler handler = new GroupMembershipsListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
				}
				@Override
				public String getSql() {
					return "select group_id,member_email,last_update from group_membership where member_email=?";
				}
			};
			executer.execute(handler);
			return results;
		}

		
		public List<Group> getUserGroups(final String ownerEmail) throws SQLException{
			final List<Group> results = new LinkedList<Group>();
			GroupListQueryHandler handler = new GroupListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
				}
				@Override
				public String getSql() {
					return "select g.group_id,g.name,g.picture,g.last_update,g.description from `group` as g join group_membership as gm on g.group_id=gm.group_id where gm.member_email=?";
				}
			};
			executer.execute(handler);
			return results;
		}

		public List<BuddyMessages> getBuddiesMessages(final String ownerEmail) throws SQLException{
			final List<BuddyMessages> results = new LinkedList<BuddyMessages>();
			BuddyMessagesListQueryHandler handler = new BuddyMessagesListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
					statement.setString(2, ownerEmail);
				}
				@Override
				public String getSql() {
					return "select send_date, sender_id, receiver_id, message, is_attachment_path from buddy_message where (sender_id=? or receiver_id=?) order by send_date";
				}
			};
			executer.execute(handler);
			return results;
		}

		public List<GroupMessages> getGroupsMessages(final String ownerEmail) throws SQLException{
			final List<GroupMessages> results = new LinkedList<GroupMessages>();
			GroupMessagesListQueryHandler handler = new GroupMessagesListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
				}
				@Override
				public String getSql() {
					return "select sender_id,receiver_id,send_date,message,is_attachment_path from group_message join (select group_id from group_membership where member_email=?) as gm on gm.group_id=receiver_id order by send_date";
				}
			};
			executer.execute(handler);
			return results;
		}

	}
	
	
	public DAL(DataSource ds){
		executer=new PreparedStatementExecuter(ds);
	}
}
