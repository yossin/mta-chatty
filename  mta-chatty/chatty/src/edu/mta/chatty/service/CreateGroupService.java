package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.domain.CreateGroupRequest;

public class CreateGroupService extends BaseService<CreateGroupRequest> {
	private static final long serialVersionUID = 1L;


	@Override
	protected CreateGroupRequest create() {
		return new CreateGroupRequest();
	}

	@Override
	protected void perform(Writer writer, CreateGroupRequest t) throws Exception {
		bl.groups.create(t);
	}

}
