package edu.mta.chatty.dal.handlers;

import java.util.List;



public interface BatchHandler {
	List<String> getBatchList();
	void handleResults(int[] result);
}