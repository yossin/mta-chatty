package edu.mta.chatty.util;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.mta.chatty.util.CloudFoundryContext.VCAPServices.VCAPService;


public class CloudFoundryContext {
	private final static Logger logger = Logger.getLogger(CloudFoundryContext.class.getName());
	private static void invoke(Object object, String methodName, String value) throws Exception{
		Method method = object.getClass().getMethod(methodName, String.class);
		method.invoke(object, value);
	}
	
	@XmlRootElement()
	public static class VCAPServices{
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class VCAPService{
			@JsonIgnoreProperties(ignoreUnknown = true)
			public static class VCAPCred{
				@JsonProperty("hostname")
				String hostname;
				@JsonProperty("username")
				String username;
				@JsonProperty("password")
				String password;
				@JsonProperty("port")
				int port;
				@JsonProperty("name")
				String dbName;
			}
			
			@JsonProperty("name")
			String name;
			@JsonProperty("credentials")
			VCAPCred credentials;
		}
		@JsonProperty("mysql-5.1")
		VCAPService[] mysqlServices;
	}
	

	private static VCAPServices loadVCAPDefinitions(){
		String env = System.getenv("VCAP_SERVICES");
		env ="{\"mysql-5.1\":[{\"name\":\"chattyDB\",\"label\":\"mysql-5.1\",\"plan\":\"free\",\"tags\":[\"relational\",\"mysql-5.1\",\"mysql\"],\"credentials\":{\"name\":\"dd7de4c8bb45b4da5a64b303c860fc2a6\",\"hostname\":\"172.30.48.30\",\"host\":\"172.30.48.30\",\"port\":3306,\"user\":\"u9ALUSdrXshXo\",\"username\":\"u9ALUSdrXshXo\",\"password\":\"pUD3ikt6WIJbR\"}}]}";
		if (env != null){
	        ByteArrayInputStream is = new ByteArrayInputStream(env.getBytes());
			ObjectMapper mapper = new ObjectMapper();
			try {
				VCAPServices data = mapper.readValue(is, VCAPServices.class);
				return data;
			} catch (Exception e) {
				logger.warning(String.format("unable to load VCAP definitions with an error %s", e));
			}
		}
		return null;
	}
	
	public static void initialize(DataSource  ds) {
		try {
			VCAPServices services = loadVCAPDefinitions();
			if (services ==null) return;
			if (services.mysqlServices == null || services.mysqlServices.length==0) return;
			
			VCAPService service = services.mysqlServices[0];
			String host = service.credentials.hostname;
			int port = service.credentials.port;
			String db=service.credentials.dbName;
			String url=String.format("jdbc:mysql://%s:%d/%s",host, port,db);
			String user=service.credentials.username;
			String pass=service.credentials.password;
			
			logger.info("setting datasource configuration with clould foundry definitions");
			invoke(ds, "setUrl", url);
			invoke(ds, "setUsername", user);
			invoke(ds, "setPassword", pass);
		}catch(Exception e){
			logger.severe(String.format("error while setting datasource configuration with clould foundry definitions. error is %s",e));
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
