package edu.mta.chatty.domain;

import java.util.List;

import edu.mta.chatty.contract.GenericDataResponse;


public class UserData implements GenericDataResponse{
	private List<Country> countries;
	private List<City> cities;
	private List<Address> addresses;
	private List<User> users;
	private List<BuddyList> buddy_list;
	private List<Group> groups;
	private List<BuddyMessages> buddy_messages;
	private List<GroupMemberships> group_memberships;
	private List<GroupMessages> group_messages;
	
	
	public List<Country> getCountries() {
		return countries;
	}
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<BuddyList> getBuddy_list() {
		return buddy_list;
	}
	public void setBuddy_list(List<BuddyList> buddy_list) {
		this.buddy_list = buddy_list;
	}
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	public List<BuddyMessages> getBuddy_messages() {
		return buddy_messages;
	}
	public void setBuddy_messages(List<BuddyMessages> buddy_messages) {
		this.buddy_messages = buddy_messages;
	}
	public List<GroupMemberships> getGroup_memberships() {
		return group_memberships;
	}
	public void setGroup_memberships(List<GroupMemberships> group_memberships) {
		this.group_memberships = group_memberships;
	}
	public List<GroupMessages> getGroup_messages() {
		return group_messages;
	}
	public void setGroup_messages(List<GroupMessages> group_messages) {
		this.group_messages = group_messages;
	}
	
	
}