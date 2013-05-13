package edu.mta.chatty.bl;

public class Admin {

	private String NumBuddies;
	private String NumGroups;
	private String NumMessages;

	public Admin(){
// TODO
		NumBuddies = "100";
		NumGroups = "150";
		NumMessages = "1500";
	}
	
	public String getNumBuddies(){
		return NumBuddies;
	}
	public String getNumGroups(){
		return NumGroups;
	}
	public String getNumMessages(){
		return NumMessages;
	}
	public void setNumBuddies(String NumBuddies){
		this.NumBuddies = NumBuddies;
	}
	public void setNumGroups(String NumGroups){
		this.NumGroups = NumGroups;
	}
	public void setNumMessages(String NumMessages){
		this.NumMessages = NumMessages;
	}
}
