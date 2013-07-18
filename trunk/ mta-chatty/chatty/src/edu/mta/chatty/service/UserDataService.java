package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.contract.UserRequest;
import edu.mta.chatty.domain.SyncUserRequest;
import edu.mta.chatty.domain.User;
import edu.mta.chatty.domain.UserData;

public class UserDataService extends BaseSyncService<UserRequest> {
	private static final long serialVersionUID = 1L;


	@Override
	protected UserRequest create() {
		return new User();
	}
	
	

	@Override
	protected void perform(SyncUserRequest request, Writer writer, UserRequest t) throws Exception {
		UserData data = bl.data.getUserData(request);
		writeJsonResponse(writer, data);
	}

}
