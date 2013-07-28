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
	public int getBuddyStatisticsSize(){
		return buddyStatistics.size();
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
	public int getGroupStatisticsSize(){
		return groupStatistics.size();
	}
	public List<DailyCountStatistic> getBuddyMessageStatistics() {
		return buddyMessageStatistics;
	}
	public void setBuddyMessageStatistics(List<DailyCountStatistic> buddyMessageStatistics) {
		this.buddyMessageStatistics = buddyMessageStatistics;
	}
	public int getBuddyMessageStatisticsSize(){
		return buddyMessageStatistics.size();
	}
	public List<DailyCountStatistic> getGroupMessageStatistics() {
		return groupMessageStatistics;
	}
	public void setGroupMessageStatistics(List<DailyCountStatistic> groupMessageStatistics) {
		this.groupMessageStatistics = groupMessageStatistics;
	}
	public int getGroupMessageStatisticsSize(){
		return groupMessageStatistics.size();
	}

	public int getMessageStatisticsSize(){
		return getBuddyMessageStatisticsSize()+getGroupMessageStatisticsSize();
	}
}
