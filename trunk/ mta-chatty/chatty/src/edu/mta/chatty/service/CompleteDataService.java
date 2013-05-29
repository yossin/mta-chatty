package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.contract.UserRequest;
import edu.mta.chatty.domain.User;
import edu.mta.chatty.domain.UserData;

public class CompleteDataService extends BaseService<UserRequest> {
	private static final long serialVersionUID = 1L;


	@Override
	protected UserRequest create() {
		return new User();
	}

	@Override
	protected void perform(Writer writer, UserRequest t) throws Exception {
		UserData data = bl.data.getCompleteUserData(t);
		writeJsonResponse(writer, data);
	}

}
