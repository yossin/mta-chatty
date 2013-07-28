package edu.mta.chatty.controller;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.mta.chatty.domain.admin.LoginInfo;
import edu.mta.chatty.service.GenericService;
import edu.mta.chatty.util.IOHelper;

public abstract class BaseAdminServlet<T> extends GenericService<T>{
	final static private Logger logger = Logger.getLogger(BaseAdminServlet.class.getName());

	private static final long serialVersionUID = -814577615549749718L;
	private final static String AdminLoginSessionAttr="admin-login";
	private Properties adminUsers;
	
	private boolean isLoggedInAsAdmin(HttpSession session){
		Boolean isLoggedIn = (Boolean) session.getAttribute(AdminLoginSessionAttr);
		if (isLoggedIn==null) return false;
		return isLoggedIn;
	}
	private void setLoggedInAsAdmin(HttpSession session){
		session.setAttribute(AdminLoginSessionAttr, true);
	}
	
	private boolean loadAdminUsersIntoCache(){
		try {
			adminUsers = IOHelper.loadProperties("admin-users.properties");
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "unable to load admin users list", e);
			return false;
		}

	}
	private boolean tryLogin(LoginInfo loginInfo){
		if (adminUsers== null){
			loadAdminUsersIntoCache();
		}
		if (adminUsers!= null && loginInfo != null){
			if (loginInfo.getUser() != null){
				String pass = adminUsers.getProperty(loginInfo.getUser());
				if (pass != null){
					return pass.equals(loginInfo.getPass());
				}
			}
		}
		return false;
	}
	
	
	private void performAsAdmin(HttpServletRequest req, HttpServletResponse resp, T t) throws Exception{
		String page = perform(req,t);
		req.getRequestDispatcher(page).forward(req, resp);
	}
	
	@Override
	protected void perform(HttpServletRequest req, HttpServletResponse resp, T t)
			throws Exception {
		HttpSession session = req.getSession();
		if (isLoggedInAsAdmin(session)){
			performAsAdmin(req, resp, t);
		} else {
			LoginInfo info = new LoginInfo();
			populate(req, info);
			if (tryLogin(info)){
				setLoggedInAsAdmin(session);
				performAsAdmin(req, resp, t);
			} else {
				req.setAttribute("action", req.getRequestURI());
				req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
			}
		}

	}
	
	protected abstract String perform (HttpServletRequest req, T t) throws Exception;
	
}
