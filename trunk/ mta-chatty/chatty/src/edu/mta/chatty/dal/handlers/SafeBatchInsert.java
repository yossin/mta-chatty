package edu.mta.chatty.dal.handlers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface SafeBatchInsert<T> {
	String getSql();
	List<T> getInsertData();
	void setVariables(PreparedStatement statement, T t) throws SQLException;
	void handleResult(int[] result);
}
