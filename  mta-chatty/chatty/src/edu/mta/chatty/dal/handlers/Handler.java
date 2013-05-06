package edu.mta.chatty.dal.handlers;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Handler{
	String getSql();
	void setVariables(PreparedStatement statement) throws SQLException;
	
}