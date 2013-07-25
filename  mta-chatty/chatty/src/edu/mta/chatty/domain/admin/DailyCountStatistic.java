package edu.mta.chatty.domain.admin;

import java.sql.Date;

public class DailyCountStatistic {

	private Date day;
	private int count;

	/**
	 * @return the end
	 */
	public Date getDay() {
		return day;
	}
	/**
	 * @param end the end to set
	 */
	public void setDay(Date end) {
		this.day = end;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

}
