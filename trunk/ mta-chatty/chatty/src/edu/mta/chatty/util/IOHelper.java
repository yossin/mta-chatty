package edu.mta.chatty.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
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
	

	private static InputStream getResource(String name){
		ClassLoader classLoader = IOHelper.class.getClassLoader();
		InputStream stream = null;
		while (classLoader!=null){
			stream = classLoader.getResourceAsStream(name);
			if (stream !=null){
				return stream;
			}
			classLoader = classLoader.getParent();
		}
		
		stream =ClassLoader.getSystemResourceAsStream(name);
		return stream;
	}
	
	public static Properties loadProperties(String fileName) throws Exception{
		InputStream stream = getResource(fileName);
		try {
			Properties properties = new Properties();
			properties.load(stream);
			return properties;
		} finally{
			close(stream);
		}

	}
	public static void copy (InputStream in, OutputStream out) throws IOException{
		try {
			
			byte buffer[] = new byte[1024];
			int len =0;
			while ((len = in.read(buffer))>0){
				out.write(buffer,0,len);
			}
		} finally{
			close(in);
			close(out);
		}
	}


	private static void close(InputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
		}
	}
	private static void close(OutputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
		}
	}
}
