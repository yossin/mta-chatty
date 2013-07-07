package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.domain.GroupMemberships;

public class JoinGroupService extends BaseService<GroupMemberships> {
	private static final long serialVersionUID = 1L;


	@Override
	protected GroupMemberships create() {
		return new GroupMemberships();
	}

	@Override
	protected void perform(Writer writer, GroupMemberships t) throws Exception {
		bl.groups.joinInto(t);
	}

}
