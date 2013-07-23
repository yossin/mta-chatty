package edu.mta.chatty.dal.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;



public interface InsertHandler extends Handler{
	void handleResults(ResultSet generatedKeys) throws SQLException;
}