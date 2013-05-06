package edu.mta.chatty.dal.handlers;

import java.sql.ResultSet;



public interface QueryHandler extends Handler{
	void handleResults(ResultSet resultSet);
}