package edu.mta.chatty.domain;

import java.sql.Timestamp;

public class GroupMessages{
	private String sender_id;
	private int receiver_id;
	private String message;
	private Timestamp send_date;
	private boolean is_attachment_path;
	
	public Timestamp getSend_date() {
		return send_date;
	}
	public void setSend_date(Timestamp send_date) {
		this.send_date = send_date;
	}
	public boolean isIs_attachment_path() {
		return is_attachment_path;
	}
	public void setIs_attachment_path(boolean is_attachment_path) {
		this.is_attachment_path = is_attachment_path;
	}
	public String getSender_id() {
		return sender_id;
	}
	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}
	public int getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(int receiver_id) {
		this.receiver_id = receiver_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}