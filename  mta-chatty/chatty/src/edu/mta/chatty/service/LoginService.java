package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.domain.User;

public class LoginService extends BaseService<User> {
	private static final long serialVersionUID = 1L;


	@Override
	protected User create() {
		return new User();
	}

	@Override
	protected void perform(Writer writer, User t) throws Exception {
		User user = bl.users.login(t);
		writeJsonResponse(writer, user);
	}

}
