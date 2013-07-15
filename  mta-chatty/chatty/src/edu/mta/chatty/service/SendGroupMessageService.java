package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.domain.GroupMessages;

public class SendGroupMessageService extends BaseService<GroupMessages> {
	private static final long serialVersionUID = 1L;


	@Override
	protected GroupMessages create() {
		return new GroupMessages();
	}

	@Override
	protected void perform(Writer writer, GroupMessages t) throws Exception {
		bl.messages.send(t);
	}

}
