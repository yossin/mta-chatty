package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.domain.SyncUserRequest;
import edu.mta.chatty.domain.UserData;

public class SyncCompleteDataService extends BaseSyncService<Void> {
	private static final long serialVersionUID = 1L;


	@Override
	protected Void create() {
		return null;
	}

	@Override
	protected void perform(SyncUserRequest syncUserRequest, Writer writer, Void t) throws Exception {
		UserData data = bl.data.getCompleteUserData(syncUserRequest);
		writeJsonResponse(writer, data);
		
	}	

}
