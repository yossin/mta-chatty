package edu.mta.chatty.domain;

import java.sql.Timestamp;

import edu.mta.chatty.contract.UserRequest;

public class SyncUserRequest implements UserRequest{
	private User user;
	private SyncTimeStampSpan syncSpan;
	public SyncUserRequest(User user, SyncTimeStampSpan syncTS) {
		this.user = user;
		this.syncSpan = syncTS;
	}
	@Override
	public String getEmail() {
		return user.getEmail();
	}
	public Timestamp getBeginTS(){
		return new Timestamp(syncSpan.getBeginTS());
	}
	public Timestamp getEndTS(){
		return new Timestamp(syncSpan.getEndTS());
	}
	public SyncTimeStampSpan getSyncSpan() {
		return syncSpan;
	}	
}
