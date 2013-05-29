package edu.mta.chatty.service;

import java.io.Writer;
import java.util.List;

import edu.mta.chatty.domain.SearchRequest;
import edu.mta.chatty.domain.User;

public class FindUsersService extends BaseService<SearchRequest> {
	private static final long serialVersionUID = 1L;


	@Override
	protected SearchRequest create() {
		return new SearchRequest();
	}

	@Override
	protected void perform(Writer writer, SearchRequest t) throws Exception {
		List<User> users = bl.users.find(t);
		writeJsonResponse(writer, users);
	}

}
