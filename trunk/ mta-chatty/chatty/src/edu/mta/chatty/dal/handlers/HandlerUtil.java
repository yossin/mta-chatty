package edu.mta.chatty.dal.handlers;

import java.sql.Statement;
import java.util.logging.Logger;

public class HandlerUtil {
	public static void logUpdateStatistics(Logger logger, int []result, String entitiesName){
		int passed=0;
		int failed=0;
		for (int i:result){
			if (i==Statement.EXECUTE_FAILED) failed++; else passed++;
		}
		logger.info(String.format("insert of %d %s passed. %d failed!", passed,entitiesName, failed));

	}
}
