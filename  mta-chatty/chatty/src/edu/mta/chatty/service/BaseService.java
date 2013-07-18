package edu.mta.chatty.service;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseService<T> extends GenericService<T>{
	private static final long serialVersionUID = -814577615549749718L;

	
	@Override
	protected void perform(HttpServletRequest req, HttpServletResponse resp, T t)
			throws Exception {
		perform(resp.getWriter(), t);
		
	}
	protected abstract void perform(Writer writer, T t) throws Exception;
	
}
