package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.domain.BuddyMessages;

public class SendBuddyMessageService extends BaseService<BuddyMessages> {
	private static final long serialVersionUID = 1L;


	@Override
	protected BuddyMessages create() {
		return new BuddyMessages();
	}

	@Override
	protected void perform(Writer writer, BuddyMessages t) throws Exception {
		bl.messages.send(t);
	}

}
