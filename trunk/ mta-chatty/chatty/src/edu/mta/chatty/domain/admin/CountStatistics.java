package edu.mta.chatty.domain.admin;

import java.util.List;

public class CountStatistics {

	private List<DailyCountStatistic> buddyStatistics;
	private List<DailyCountStatistic> groupStatistics;
	private List<DailyCountStatistic> buddyMessageStatistics;
	private List<DailyCountStatistic> groupMessageStatistics;
	/**
	 * @return the buddyStatistics
	 */
	public List<DailyCountStatistic> getBuddyStatistics() {
		return buddyStatistics;
	}
	/**
	 * @param buddyStatistics the buddyStatistics to set
	 */
	public void setBuddyStatistics(List<DailyCountStatistic> buddyStatistics) {
		this.buddyStatistics = buddyStatistics;
	}
	/**
	 * @return the groupStatistics
	 */
	public List<DailyCountStatistic> getGroupStatistics() {
		return groupStatistics;
	}
	/**
	 * @param groupStatistics the groupStatistics to set
	 */
	public void setGroupStatistics(List<DailyCountStatistic> groupStatistics) {
		this.groupStatistics = groupStatistics;
	}
	public List<DailyCountStatistic> getBuddyMessageStatistics() {
		return buddyMessageStatistics;
	}
	public void setBuddyMessageStatistics(List<DailyCountStatistic> buddyMessageStatistics) {
		this.buddyMessageStatistics = buddyMessageStatistics;
	}
	public List<DailyCountStatistic> getGroupMessageStatistics() {
		return groupMessageStatistics;
	}
	public void setGroupMessageStatistics(List<DailyCountStatistic> groupMessageStatistics) {
		this.groupMessageStatistics = groupMessageStatistics;
	}

}
