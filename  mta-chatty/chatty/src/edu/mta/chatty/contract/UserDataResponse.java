package edu.mta.chatty.contract;

import java.util.List;

import edu.mta.chatty.domain.Address;
import edu.mta.chatty.domain.BuddyList;
import edu.mta.chatty.domain.BuddyMessages;
import edu.mta.chatty.domain.Group;
import edu.mta.chatty.domain.GroupMemberships;
import edu.mta.chatty.domain.GroupMessages;
import edu.mta.chatty.domain.User;

public interface UserDataResponse extends GenericDataResponse{
	List<Address> getAddresses();
	List<User> getUsers();
	List<BuddyList> getBuddy_list();
	List<Group> getGroups();
	List<BuddyMessages> getBuddy_messages();
	List<GroupMemberships> getGroup_memberships();
	List<GroupMessages> getGroup_messages();
}