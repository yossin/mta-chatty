package edu.mta.chatty.service;

import java.io.Writer;
import java.util.List;

import edu.mta.chatty.domain.Group;
import edu.mta.chatty.domain.SearchRequest;

public class FindGroupsService extends BaseService<SearchRequest> {
	private static final long serialVersionUID = 1L;


	@Override
	protected SearchRequest create() {
		return new SearchRequest();
	}

	@Override
	protected void perform(Writer writer, SearchRequest t) throws Exception {
		List<Group> users = bl.groups.find(t);
		writeJsonResponse(writer, users);
	}

}
