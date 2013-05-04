package edu.mta.chatty.dal;

import java.sql.PreparedStatement;

public interface Handler{
	String getSql();
	void setVariables(PreparedStatement statement);
	
}