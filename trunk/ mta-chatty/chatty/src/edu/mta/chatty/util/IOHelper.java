package edu.mta.chatty.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class IOHelper {
	private static Logger logger = Logger.getLogger(IOHelper.class.getName());
	public static String readAsString(InputStream is) throws IOException{
		BufferedReader reader =null;
		try {
			StringBuilder builder = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(is));
			String line=null;
			while((line=reader.readLine())!= null){
				builder.append(line);
			}
			return builder.toString();
		} finally{
			close(reader);
		}
	}
	
	private static void close(BufferedReader reader){
		try {
			reader.close();
		} catch (IOException e) {
			logger.warning("unable to close buffered reader "+e);
		}
	}
}
