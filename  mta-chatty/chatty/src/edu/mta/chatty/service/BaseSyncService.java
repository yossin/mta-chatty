package edu.mta.chatty.service;

import java.io.Writer;

import javax.servlet.http.HttpSession;

import edu.mta.chatty.domain.SyncTimeStampSpan;
import edu.mta.chatty.domain.SyncUserRequest;
import edu.mta.chatty.domain.User;


public abstract class BaseSyncService<T> extends BaseSessionService<T>{

	private static final long serialVersionUID = -1216078385476226505L;

	protected User getUser(HttpSession session) throws Exception{
		User user = (User) session.getAttribute(LoginService.UserAttrName);
		
		if (user==null){
			throw new Exception ("your session has expired. please login");
		}
		return user;
	}
	
	
	protected SyncTimeStampSpan getSyncSpan(HttpSession session) throws Exception{
		SyncTimeStampSpan ts = (SyncTimeStampSpan) session.getAttribute(LoginService.SyncSpanAttrName);
		if (ts==null){
			throw new Exception ("sync span is null. please login");
		}
		ts.update();
		return ts;
	}

	@Override
	protected void perform(HttpSession session, Writer writer, T t) throws Exception {
		User user = getUser(session);
		SyncTimeStampSpan syncSpan = getSyncSpan(session);
		SyncUserRequest request = new SyncUserRequest(user, syncSpan);
		perform(request, writer, t);
	}

	abstract protected void perform(SyncUserRequest syncUserRequest, Writer writer, T t)throws Exception;

	
}
