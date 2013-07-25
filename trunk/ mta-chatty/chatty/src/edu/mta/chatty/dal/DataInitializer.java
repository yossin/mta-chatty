package edu.mta.chatty.dal;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.mta.chatty.dal.handlers.HandlerUtil;
import edu.mta.chatty.dal.handlers.SafeBatchInsert;
import edu.mta.chatty.domain.Address;
import edu.mta.chatty.domain.BuddyList;
import edu.mta.chatty.domain.BuddyMessages;
import edu.mta.chatty.domain.City;
import edu.mta.chatty.domain.Country;
import edu.mta.chatty.domain.Group;
import edu.mta.chatty.domain.GroupMemberships;
import edu.mta.chatty.domain.GroupMessages;
import edu.mta.chatty.domain.UserData;
import edu.mta.chatty.domain.User;

public class DataInitializer {
	private final static Logger logger = Logger.getLogger(DataInitializer.class.getName());
	final static String fileName = "WEB-INF/test/test-insert.json";
	private PreparedStatementExecuter batchInsert;

	private static abstract class InsertHandler<T> implements
			SafeBatchInsert<T> {
		@Override
		public void handleResult(int[] result) {
			HandlerUtil.logUpdateStatistics(logger, result, getEntitiesName());
		}

		abstract String getEntitiesName();
	}

	public DataInitializer(DataSource ds) {
		this.batchInsert = new PreparedStatementExecuter(ds);
	}


	private void insertCountries(final List<Country> data) throws SQLException {
		batchInsert.execute(new InsertHandler<Country>() {
			@Override
			public String getSql() {
				return "insert into country (country_id,country) values (?,?)";
			}

			@Override
			public List<Country> getInsertData() {
				return data;
			}

			@Override
			public void setVariables(PreparedStatement statement, Country t)
					throws SQLException {
				statement.setInt(1, t.getCountry_id());
				statement.setString(2, t.getCountry());
			}

			@Override
			String getEntitiesName() {
				return "countries";
			}
		});
	}

	private void insertCities(final List<City> data) throws SQLException {
		batchInsert.execute(new InsertHandler<City>() {
			@Override
			public String getSql() {
				return "insert into city (city_id, city, country_id) values (?,?,?)";
			}

			@Override
			public List<City> getInsertData() {
				return data;
			}

			@Override
			public void setVariables(PreparedStatement statement, City t)
					throws SQLException {
				statement.setInt(1, t.getCity_id());
				statement.setString(2, t.getCity());
				statement.setInt(3, t.getCountry_id());
			}

			@Override
			String getEntitiesName() {
				return "cities";
			}
		});
	}

	private void insertAddresses(final List<Address> data) throws SQLException {
		batchInsert.execute(new InsertHandler<Address>() {
			@Override
			public String getSql() {
				return "insert into address (address_id, address, city_id) values (?,?,?)";
			}

			@Override
			public List<Address> getInsertData() {
				return data;
			}

			@Override
			public void setVariables(PreparedStatement statement, Address t)
					throws SQLException {
				statement.setString(1, t.getAddress_id());
				statement.setString(2, t.getAddress());
				statement.setInt(3, t.getCity_id());
			}

			@Override
			String getEntitiesName() {
				return "addresses";
			}
		});
	}

	private void insertUsers(final List<User> data) throws SQLException {
		batchInsert.execute(new InsertHandler<User>() {
			@Override
			public String getSql() {
				return "insert into `user` (email, name, picture, password,creation_timestamp) values (?,?,?,?,?)";
			}

			@Override
			public List<User> getInsertData() {
				return data;
			}

			@Override
			public void setVariables(PreparedStatement statement, User t)
					throws SQLException {
				statement.setString(1, t.getEmail());
				statement.setString(2, t.getName());
				statement.setString(3, t.getPicture());
				statement.setString(4, t.getPassword());
				statement.setTimestamp(5, t.getCreation_timestamp());
			}

			@Override
			String getEntitiesName() {
				return "users";
			}
		});
	}

	private void insertBuddyList(final List<BuddyList> data)
			throws SQLException {
		batchInsert.execute(new InsertHandler<BuddyList>() {
			@Override
			public String getSql() {
				return "insert into buddy_list (buddy_id, owner_email) values (?,?)";
			}

			@Override
			public List<BuddyList> getInsertData() {
				return data;
			}

			@Override
			public void setVariables(PreparedStatement statement, BuddyList t)
					throws SQLException {
				statement.setString(1, t.getBuddy_id());
				statement.setString(2, t.getOwner_email());
			}

			@Override
			String getEntitiesName() {
				return "buddy_list";
			}
		});
	}

	private void insertGroups(final List<Group> data) throws SQLException {
		batchInsert.execute(new InsertHandler<Group>() {
			@Override
			public String getSql() {
				return "insert into `group` (name, picture, description, creation_timestamp) values (?,?,?,?)";
			}

			@Override
			public List<Group> getInsertData() {
				return data;
			}

			@Override
			public void setVariables(PreparedStatement statement, Group t)
					throws SQLException {
				statement.setString(1, t.getName());
				statement.setString(2, t.getPicture());
				statement.setString(3, t.getDescription());
				statement.setTimestamp(4, t.getCreation_timestamp());
			}

			@Override
			String getEntitiesName() {
				return "groups";
			}
		});
	}

	private void insertBuddyMessages(final List<BuddyMessages> data)
			throws SQLException {
		batchInsert.execute(new InsertHandler<BuddyMessages>() {
			@Override
			public String getSql() {
				return "insert into buddy_message (sender_id, receiver_id, message, send_date) values (?,?,?,?)";
			}

			@Override
			public List<BuddyMessages> getInsertData() {
				return data;
			}

			@Override
			public void setVariables(PreparedStatement statement,
					BuddyMessages t) throws SQLException {
				statement.setString(1, t.getSender_id());
				statement.setString(2, t.getReceiver_id());
				statement.setString(3, t.getMessage());
				statement.setTimestamp(4, t.getSend_date());
			}

			@Override
			String getEntitiesName() {
				return "buddy_messages";
			}
		});
	}

	private void insertGroupMemberships(final List<GroupMemberships> data)
			throws SQLException {
		batchInsert.execute(new InsertHandler<GroupMemberships>() {
			@Override
			public String getSql() {
				return "insert into group_membership (member_email, group_id) values (?,?)";
			}

			@Override
			public List<GroupMemberships> getInsertData() {
				return data;
			}

			@Override
			public void setVariables(PreparedStatement statement,
					GroupMemberships t) throws SQLException {
				statement.setString(1, t.getMember_email());
				statement.setInt(2, t.getGroup_id());
			}

			@Override
			String getEntitiesName() {
				return "group_memberships";
			}
		});
	}

	private void insertGroupMessages(final List<GroupMessages> data)
			throws SQLException {
		batchInsert.execute(new InsertHandler<GroupMessages>() {
			@Override
			public String getSql() {
				return "insert into group_message (sender_id, receiver_id, message, send_date) values (?,?,?,?)";
			}

			@Override
			public List<GroupMessages> getInsertData() {
				return data;
			}

			@Override
			public void setVariables(PreparedStatement statement,
					GroupMessages t) throws SQLException {
				statement.setString(1, t.getSender_id());
				statement.setInt(2, t.getReceiver_id());
				statement.setString(3, t.getMessage());
				statement.setTimestamp(4, t.getSend_date());
			}

			@Override
			String getEntitiesName() {
				return "group_messages";
			}
		});
	}

	private InputStream getTestDataResourceFile(ServletContext context) {
		return context.getResourceAsStream(fileName);
	}

	private void insertInitData(UserData data) throws SQLException {
		insertCountries(data.getCountries());
		insertCities(data.getCities());
		insertUsers(data.getUsers());
		insertAddresses(data.getAddresses());
		insertBuddyList(data.getBuddy_list());
		insertGroups(data.getGroups());
		insertGroupMemberships(data.getGroup_memberships());
		insertBuddyMessages(data.getBuddy_messages());
		insertGroupMessages(data.getGroup_messages());
	}
	
	private static class CreationTimestampGenerator{
		private static final long dayDiff=86400000;
		private long currentTimestamp;
		private final int threshold;
		private int counter=0;
		CreationTimestampGenerator(int threshold){
			this(threshold,System.currentTimeMillis());
		}
		CreationTimestampGenerator(int threshold, long startTimestamp){
			this.threshold=threshold-1;
			this.currentTimestamp=startTimestamp;
			this.counter=threshold;
		}
		long generate(){
			if (counter-- == 0) {
				counter = threshold;
				currentTimestamp-=dayDiff;
			}
			return currentTimestamp;
		}
		long getCurrentTimestamp() {
			return currentTimestamp;
		}
		
	}
	private void manipulateCreationTimestamp(UserData data){
		
		CreationTimestampGenerator msgCTen = new CreationTimestampGenerator(1);
		for(GroupMessages msg: data.getGroup_messages()){
			Timestamp creation_timestamp = new Timestamp(msgCTen.generate());
			msg.setSend_date(creation_timestamp);
		}

		for(BuddyMessages msg: data.getBuddy_messages()){
			Timestamp creation_timestamp = new Timestamp(msgCTen.generate());
			msg.setSend_date(creation_timestamp);
		}

		
		CreationTimestampGenerator groupCTen = new CreationTimestampGenerator(1, msgCTen.getCurrentTimestamp());
		for(Group group: data.getGroups()){
			Timestamp creation_timestamp = new Timestamp(groupCTen.generate());
			group.setCreation_timestamp(creation_timestamp);
		}
		
		CreationTimestampGenerator userCTen = new CreationTimestampGenerator(2, groupCTen.getCurrentTimestamp());
		for(User user: data.getUsers()){
			Timestamp creation_timestamp = new Timestamp(userCTen.generate());
			user.setCreation_timestamp(creation_timestamp);
		}

	}

	public boolean loadInitData(ServletContext context) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			InputStream is = getTestDataResourceFile(context);
			UserData data = mapper.readValue(is, UserData.class);
			manipulateCreationTimestamp(data);
			insertInitData(data);
			return true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}