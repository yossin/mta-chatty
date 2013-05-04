package edu.mta.chatty.dal;

import java.sql.ResultSet;


public interface QueryHandler extends Handler{
	void handleResults(ResultSet resultSet);
}