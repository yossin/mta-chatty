package edu.mta.chatty.dal;

import java.util.List;



public interface BatchHandler {
	List<String> getBatchList();
	void handleResults(int[] result);
}