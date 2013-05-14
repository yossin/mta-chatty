package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.contract.UserDataResponse;
import edu.mta.chatty.contract.UserRequest;
import edu.mta.chatty.domain.User;

public class UserDataService extends BaseService<UserRequest> {
	private static final long serialVersionUID = 1L;


	@Override
	protected UserRequest create() {
		return new User();
	}

	@Override
	protected void perform(Writer writer, UserRequest t) throws Exception {
		UserDataResponse data = bl.data.getUserData(t);
		writeJsonResponse(writer, data);
	}

}
