package edu.mta.chatty.domain;

import java.sql.Timestamp;


public class Group extends Updatable{
	private int group_id;
	private String name;
	private String picture;
	private String description;
	private Timestamp creation_timestamp;


	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreation_timestamp() {
		return creation_timestamp;
	}
	public void setCreation_timestamp(Timestamp creation_timestamp) {
		this.creation_timestamp = creation_timestamp;
	}
}