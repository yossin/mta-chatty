package edu.mta.chatty.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import edu.mta.chatty.contract.GenericDataResponse;
import edu.mta.chatty.contract.LoginRequest;
import edu.mta.chatty.dal.handlers.GenericListQueryHandler;
import edu.mta.chatty.dal.handlers.InsertHandler;
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
import edu.mta.chatty.domain.admin.CountStatistics;
import edu.mta.chatty.domain.admin.DailyCountStatistic;
import edu.mta.chatty.domain.admin.DateRangeRequest;

public class DAL {
	private final static Logger logger = Logger.getLogger(DAL.class.getName());
	private PreparedStatementExecuter executer;
	final public Users users = new Users();
	final public Data data = new Data();
	final public Groups groups = new Groups();
	final public Messages messages = new Messages();
	final public Admin admin = new Admin();
	
	
	private static void setLastUpdate(PreparedStatement statement, int index) throws SQLException{
		statement.setTimestamp(index, new Timestamp(System.currentTimeMillis()));
	}
	
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
	private static abstract class CountStatisticListQueryHandler extends GenericListQueryHandler<DailyCountStatistic>{
		CountStatisticListQueryHandler(List<DailyCountStatistic> results){
			super(results);
		}
		@Override
		protected DailyCountStatistic create() {
			return new DailyCountStatistic();
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
					setLastUpdate(statement, 3);
				}
				@Override
				public String getSql() {
					return "insert into buddy_list (owner_email, buddy_id,last_update) values (?,?,?)";
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
					setLastUpdate(statement, 5);

				}
				@Override
				public String getSql() {
					return "insert into `user` (email, name, picture, password,last_update) values (?,?,?,?,?)";
				}
				
				@Override
				public void handleResults(int result) {
				}
			};
			executer.execute(handler);
		}
		
		
		public void updateUserImage(final String userId, final String imageName) throws SQLException{
			UpdateHandler handler = new UpdateHandler() {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1,imageName);
					statement.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
					statement.setString(3,userId);
				}
				@Override
				public String getSql() {
					return "update `user` set picture=?, last_update=? where email=?";
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
					setLastUpdate(statement, 3);
				}
				@Override
				public String getSql() {
					return "insert into group_membership (member_email, group_id,last_update) values (?,?,?)";
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

		public void create(final Group group) throws SQLException{
			InsertHandler handler = new InsertHandler() {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1,group.getName());
					statement.setString(2,group.getPicture());
					statement.setString(3,group.getDescription());
					setLastUpdate(statement, 4);

				}
				@Override
				public String getSql() {
					return "insert into `group` (name, picture, description,last_update) values (?,?,?,?)";
				}
				
				@Override
				public void handleResults(ResultSet generatedKeys) throws SQLException{
			        if (generatedKeys.next()) {
			            group.setGroup_id(generatedKeys.getInt(1));
			        } else {
			            throw new SQLException("Creating group failed, no generated key obtained.");
			        }

				}
			};
			executer.execute(handler);
		}

		public void updateGroupImage(final int groupId, final String imageName) throws SQLException{
			UpdateHandler handler = new UpdateHandler() {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1,imageName);
					statement.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
					statement.setInt(3,groupId);
				}
				@Override
				public String getSql() {
					return "update `group` set picture=?, last_update=? where group_id=?";
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
		private List<Country> getCountries(final Timestamp begin, final Timestamp end) throws SQLException{
			final List<Country> results = new LinkedList<Country>();
			CountryListQueryHandler handler = new CountryListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setTimestamp(1, begin);
					statement.setTimestamp(2, end);
				}
				@Override
				public String getSql() {
					return "select country_id,country,last_update from country where (last_update >=? and last_update <?)";
				}
			};
			executer.execute(handler);
			return results;
		}
		
		private List<City> getCities(final Timestamp begin, final Timestamp end) throws SQLException{
			final List<City> results = new LinkedList<City>();
			CityListQueryHandler handler = new CityListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setTimestamp(1, begin);
					statement.setTimestamp(2, end);
				}
				@Override
				public String getSql() {
					return "select city_id,city,country_id,last_update from city where (last_update >=? and last_update <?)";
				}
			};
			executer.execute(handler);
			return results;
		}
		
		public GenericDataResponse getGenericData(final Timestamp begin, final Timestamp end) throws SQLException{
			UserData results = new UserData();
			results.setCountries(getCountries(begin, end));
			results.setCities(getCities(begin, end));
			return results;
		}
		
		public List<User> getUsers(final String ownerEmail, final Timestamp begin, final Timestamp end) throws SQLException{
			final List<User> results = new LinkedList<User>();
			UserListQueryHandler handler = new UserListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					int i=1;
					statement.setString(i++, ownerEmail);
					statement.setTimestamp(i++, begin);
					statement.setTimestamp(i++, end);
					statement.setTimestamp(i++, begin);
					statement.setTimestamp(i++, end);
					statement.setString(i++, ownerEmail);
					statement.setTimestamp(i++, begin);
					statement.setTimestamp(i++, end);
				}
				@Override
				public String getSql() {
					return "select u.email,u.name,u.picture,u.active,u.last_update,u.creation_timestamp from `user` as u join (select g.member_email from group_membership as g join (select group_id from group_membership where member_email=? and (last_update >=? and last_update <?)) as z on g.group_id=z.group_id where (g.last_update >=? and g.last_update <?) union select u.email from `user` as u join buddy_list as b on u.email=b.buddy_id where (b.owner_email=? and (b.last_update >=? and b.last_update <?))) as z on u.email=z.member_email";
				}
			};
			executer.execute(handler);
			return results;
		}
		
		public List<BuddyList> getBuddyList(final String ownerEmail, final Timestamp begin, final Timestamp end) throws SQLException{
			final List<BuddyList> results = new LinkedList<BuddyList>();
			BuddyListListQueryHandler handler = new BuddyListListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
					statement.setTimestamp(2, begin);
					statement.setTimestamp(3, end);
				}
				@Override
				public String getSql() {
					return "select owner_email,buddy_id,last_update from buddy_list where owner_email=? and (last_update >=? and last_update <?)";
				}
			};
			executer.execute(handler);
			return results;
		}

		public List<GroupMemberships> getGroupList(final String ownerEmail, final Timestamp begin, final Timestamp end) throws SQLException{
			final List<GroupMemberships> results = new LinkedList<GroupMemberships>();
			GroupMembershipsListQueryHandler handler = new GroupMembershipsListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
					statement.setTimestamp(2, begin);
					statement.setTimestamp(3, end);
				}
				@Override
				public String getSql() {
					return "select group_id,member_email,last_update from group_membership where member_email=? and (last_update >=? and last_update <?)";
				}
			};
			executer.execute(handler);
			return results;
		}

		
		public List<Group> getUserGroups(final String ownerEmail, final Timestamp begin, final Timestamp end) throws SQLException{
			final List<Group> results = new LinkedList<Group>();
			GroupListQueryHandler handler = new GroupListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
					statement.setTimestamp(2, begin);
					statement.setTimestamp(3, end);
				}
				@Override
				public String getSql() {
					return "select g.group_id,g.name,g.picture,g.last_update,g.creation_timestamp,g.description from `group` as g join group_membership as gm on g.group_id=gm.group_id where gm.member_email=? and (gm.last_update >=? and gm.last_update <?)";
				}
			};
			executer.execute(handler);
			return results;
		}

		public List<BuddyMessages> getBuddiesMessages(final String ownerEmail, final Timestamp begin, final Timestamp end) throws SQLException{
			final List<BuddyMessages> results = new LinkedList<BuddyMessages>();
			BuddyMessagesListQueryHandler handler = new BuddyMessagesListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
					statement.setString(2, ownerEmail);
					statement.setTimestamp(3, begin);
					statement.setTimestamp(4, end);
				}
				@Override
				public String getSql() {
					return "select send_date, sender_id, receiver_id, message, is_attachment_path from buddy_message where ((sender_id=? or receiver_id=?) and (send_date >=? and send_date <?)) order by send_date";
				}
			};
			executer.execute(handler);
			return results;
		}

		public List<GroupMessages> getGroupsMessages(final String ownerEmail, final Timestamp begin, final Timestamp end) throws SQLException{
			final List<GroupMessages> results = new LinkedList<GroupMessages>();
			GroupMessagesListQueryHandler handler = new GroupMessagesListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1, ownerEmail);
					statement.setTimestamp(2, begin);
					statement.setTimestamp(3, end);
				}
				@Override
				public String getSql() {
					return "select sender_id,receiver_id,send_date,message,is_attachment_path from group_message join (select group_id from group_membership where member_email=?) as gm on gm.group_id=receiver_id where (send_date >=? and send_date <?) order by send_date";
				}
			};
			executer.execute(handler);
			return results;
		}

	}
	public class Messages{
		public void send(final GroupMessages message) throws SQLException{
			UpdateHandler handler = new UpdateHandler() {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1,message.getSender_id());
					statement.setInt(2,message.getReceiver_id());
					statement.setString(3, message.getMessage());
				}
				@Override
				public String getSql() {
					return "insert into group_message (sender_id, receiver_id, message) values(?,?,?)";
				}
				
				@Override
				public void handleResults(int result) {
				}
			};
			executer.execute(handler);
		}
		
		public void send(final BuddyMessages message) throws SQLException{
			UpdateHandler handler = new UpdateHandler() {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setString(1,message.getSender_id());
					statement.setString(2,message.getReceiver_id());
					statement.setString(3, message.getMessage());
				}
				@Override
				public String getSql() {
					return "insert into buddy_message (sender_id, receiver_id, message) values(?,?,?)";
				}
				
				@Override
				public void handleResults(int result) {
				}
			};
			executer.execute(handler);
		}

	}
	
	public class Admin{
		private List<DailyCountStatistic> getUserStatistics(final DateRangeRequest request) throws SQLException{
			return getGenericCreationDateStatistics(request, "user");
		}
		private List<DailyCountStatistic> getGroupStatistics(final DateRangeRequest request) throws SQLException{
			return getGenericCreationDateStatistics(request, "group");
		}

		private List<DailyCountStatistic> getGenericCreationDateStatistics(final DateRangeRequest request, final String tableName) throws SQLException{
			final List<DailyCountStatistic> results = new LinkedList<DailyCountStatistic>();
			CountStatisticListQueryHandler handler = new CountStatisticListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setDate(1, request.getStart());
					statement.setDate(2, request.getEnd());
				}
				@Override
				public String getSql() {
					return String.format("SELECT count(creation_timestamp) as `count` ,date(creation_timestamp) as `day` FROM `%s` where creation_timestamp>= ? and creation_timestamp <= ? group by date(creation_timestamp)",tableName);
				}
			};
			executer.execute(handler);
			return results;
		}

		private List<DailyCountStatistic> getGenericSendDateStatistics(final DateRangeRequest request, final String tableName) throws SQLException{
			final List<DailyCountStatistic> results = new LinkedList<DailyCountStatistic>();
			CountStatisticListQueryHandler handler = new CountStatisticListQueryHandler(results) {
				@Override
				public void setVariables(PreparedStatement statement) throws SQLException {
					statement.setDate(1, request.getStart());
					statement.setDate(2, request.getEnd());
				}
				@Override
				public String getSql() {
					return String.format("SELECT count(send_date) as `count` ,date(send_date) as `day` FROM `%s` where send_date>= ? and send_date <= ? group by date(send_date)",tableName);
				}
			};
			executer.execute(handler);
			return results;
		}
		private List<DailyCountStatistic> getUserMessageStatistics(final DateRangeRequest request) throws SQLException{
			return getGenericSendDateStatistics(request, "buddy_message");
		}
		private List<DailyCountStatistic> getGroupMessageStatistics(final DateRangeRequest request) throws SQLException{
			return getGenericSendDateStatistics(request, "group_message");
		}

	
		public CountStatistics getCountStatistics(final DateRangeRequest request) throws SQLException{
			CountStatistics statistics = new CountStatistics();
			statistics.setBuddyStatistics(getUserStatistics(request));
			statistics.setGroupStatistics(getGroupStatistics(request));
			statistics.setBuddyMessageStatistics(getUserMessageStatistics(request));
			statistics.setGroupMessageStatistics(getGroupMessageStatistics(request));
			return statistics;
		}
	}
	
	public DAL(DataSource ds){
		executer=new PreparedStatementExecuter(ds);
	}
}
