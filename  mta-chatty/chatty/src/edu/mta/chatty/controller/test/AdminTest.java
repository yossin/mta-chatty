package edu.mta.chatty.controller.test;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


import edu.mta.chatty.Constants;
import edu.mta.chatty.bl.BL;
import edu.mta.chatty.domain.admin.CountStatistics;
import edu.mta.chatty.domain.admin.DateRangeRequest;

public class AdminTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name = Constants.DataSource)
	private DataSource ds;
    protected BL bl;
	public void init(ServletConfig config) throws ServletException {
		bl = new BL(ds);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DateRangeRequest t = new DateRangeRequest();
		
		t.setStart(new Date(new java.util.Date("2013/07/01").getTime()));
		t.setEnd(new Date(new java.util.Date("2013/08/01").getTime()));
		CountStatistics stat = null;
		try {
			stat = bl.admin.getCountStatistics(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().print("hello");
	}


}
