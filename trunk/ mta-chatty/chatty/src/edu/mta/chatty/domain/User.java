package edu.mta.chatty.domain;

import java.sql.Timestamp;

import edu.mta.chatty.contract.LoginRequest;

public class User extends Updatable implements LoginRequest{
	private String email;
	private String name;
	private String picture;
	private String password;
	private boolean active=true;
	private Timestamp creation_timestamp;
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getCreation_timestamp() {
		return creation_timestamp;
	}
	public void setCreation_timestamp(Timestamp creation_timestamp) {
		this.creation_timestamp = creation_timestamp;
	}
}