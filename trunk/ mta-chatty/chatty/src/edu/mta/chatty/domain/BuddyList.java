package edu.mta.chatty.domain;

public class BuddyList{
	private String buddy_id;
	private String owner_email;
	public String getBuddy_id() {
		return buddy_id;
	}
	public void setBuddy_id(String buddy_id) {
		this.buddy_id = buddy_id;
	}
	public String getOwner_email() {
		return owner_email;
	}
	public void setOwner_email(String owner_email) {
		this.owner_email = owner_email;
	}
}