package edu.mta.chatty.service;

import java.io.Writer;

import javax.servlet.http.HttpSession;

import edu.mta.chatty.domain.SyncTimeStampSpan;
import edu.mta.chatty.domain.User;

public class LoginService extends BaseSessionService<User> {
	private static final long serialVersionUID = 1L;
	final static String UserAttrName="user";
	final static String SyncSpanAttrName="syncSpan";

	@Override
	protected User create() {
		return new User();
	}
	
	private void saveInSession(HttpSession session, User user){
		session.setAttribute(UserAttrName, user);
		session.setAttribute(SyncSpanAttrName, new SyncTimeStampSpan());
	}

	@Override
	protected void perform(HttpSession session, Writer writer, User t) throws Exception {
		User user = bl.users.login(t);
		saveInSession(session, t);
		writeJsonResponse(writer, user);
	}

}
