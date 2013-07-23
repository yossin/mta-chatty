package edu.mta.chatty.domain;

public class CreateGroupRequest {
	private Group group = new Group();
	private GroupMemberships memberships=new GroupMemberships();
	public Group getGroup() {
		return group;
	}
	public GroupMemberships getMemberships() {
		return memberships;
	}
	public void setDescription(String description){
		group.setDescription(description);
	}
	public void setName(String name){
		group.setName(name);
	}
	public void setPicture(String picture){
		group.setPicture(picture);
	}
	public void setMember_email(String member_email){
		memberships.setMember_email(member_email);
	}
	
}
