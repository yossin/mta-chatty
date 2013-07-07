package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.domain.BuddyList;

public class AddBuddyService extends BaseService<BuddyList> {
	private static final long serialVersionUID = 1L;


	@Override
	protected BuddyList create() {
		return new BuddyList();
	}

	@Override
	protected void perform(Writer writer, BuddyList t) throws Exception {
		bl.users.addBuddy(t);
	}

}
