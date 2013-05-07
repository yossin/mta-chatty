package edu.mta.chatty.dal.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;



public interface QueryHandler extends Handler{
	void handleResults(ResultSet resultSet) throws SQLException;
}