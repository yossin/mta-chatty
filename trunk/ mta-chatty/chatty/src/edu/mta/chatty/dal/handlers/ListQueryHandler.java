package edu.mta.chatty.dal.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class ListQueryHandler<T> implements QueryHandler{

	protected List<T> results;
	protected ListQueryHandler(List<T> results){
		this.results=results;
	}
	@Override
	public void handleResults(ResultSet resultSet) throws SQLException{
		while (resultSet.next()){
			results.add(handleResult(resultSet));
		}
	}
	
	abstract protected T handleResult(ResultSet resultSet) throws SQLException;
}
