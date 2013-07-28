package edu.mta.chatty.controller;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import edu.mta.chatty.domain.admin.CountStatistics;
import edu.mta.chatty.domain.admin.DateRangeRequest;

public class AdminStatisticsServlet extends BaseAdminServlet<DateRangeRequest> {
	private static final long serialVersionUID = 1L;
       
	@Override
	protected String perform(HttpServletRequest req, DateRangeRequest t) throws Exception{
		Calendar c1 = new GregorianCalendar(2013, 1, 1);
		Calendar c2 = new GregorianCalendar(2014, 1, 1);
		
		t.setStart(new Date(c1.getTimeInMillis()));
		t.setEnd(new Date(c2.getTimeInMillis()));
		CountStatistics stat = bl.admin.getCountStatistics(t);
		req.setAttribute("statistics", stat);
		return "/jsp/admin.jsp";
	}

	@Override
	protected DateRangeRequest create() {
		return new DateRangeRequest();
	}

}
