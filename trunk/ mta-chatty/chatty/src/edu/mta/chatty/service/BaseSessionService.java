package edu.mta.chatty.service;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public abstract class BaseSessionService<T> extends GenericService<T>{

	private static final long serialVersionUID = -8468285409203987231L;

	
	@Override
	protected void perform(HttpServletRequest req, HttpServletResponse resp, T t)
			throws Exception {
		perform(req.getSession(), resp.getWriter(), t);
	}
	
	abstract protected void perform(HttpSession session, Writer writer, T t)throws Exception;

}
