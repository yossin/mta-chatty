package edu.mta.chatty.domain;

import java.sql.Timestamp;

public class Updatable{
	private Timestamp last_update;
	public Timestamp getLast_update() {
		return last_update;
	}
	public void setLast_update(Timestamp last_update) {
		this.last_update = last_update;
	}
}