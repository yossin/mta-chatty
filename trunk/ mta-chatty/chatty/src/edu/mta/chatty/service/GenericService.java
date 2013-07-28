package edu.mta.chatty.service;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.mta.chatty.Constants;
import edu.mta.chatty.bl.BL;

public abstract class GenericService<T> extends HttpServlet{
	private static final long serialVersionUID = -814577615549749718L;
	final static private Logger logger = Logger.getLogger(GenericService.class.getName());
	@Resource(name = Constants.DataSource)
	private DataSource ds;
    protected BL bl;
	public void init(ServletConfig config) throws ServletException {
		bl = new BL(ds);
	}
	
	protected abstract T create();
	protected abstract void perform(HttpServletRequest req, HttpServletResponse resp, T t) throws Exception;
	
	protected  T populate(HttpServletRequest req){
		T t = create();
		populate(req, t);
		return t;
	}
	
	protected  void populate(HttpServletRequest req, Object obj){
		Enumeration<String> names = req.getParameterNames();
		while (names.hasMoreElements()){
			String name = names.nextElement();
			Object value = req.getParameter(name);
			if (value != null){
				try {
					BeanUtils.setProperty(obj, name, value);
				} catch (Exception e) {
					String msg = String.format("unable to set parameter %s=%s for class %s", name,value, obj.getClass());
					logger.finer(msg);
					//throw new IllegalArgumentException(msg, e);
				}
			}
			
		}
	}	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		T t = populate(req);
		try {
			perform(req, resp, t);
		} catch (Exception e) {
			String msg = String.format("error while performing action %s", e);
			logger.severe(msg);
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new ServletException(e);
		}
	}
	
	protected void writeJsonResponse(Writer writer , Object obj) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(writer, obj);
	}
	
}
