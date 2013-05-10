package edu.mta.chatty;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import edu.mta.chatty.bl.Initializer;
import edu.mta.chatty.util.CloudFoundryContext;



public class AppWatcher  implements ServletContextListener {

	private final static Logger logger = Logger.getLogger(AppWatcher.class.getName());
	
	@Resource(name = Constants.DataSource)
	DataSource ds;

	
	public void contextInitialized(ServletContextEvent event) {
		
		CloudFoundryContext.initialize(ds);
		
		Initializer initializer = new Initializer(ds);
		logger.info("initializing chatty");
		ServletContext context = event.getServletContext();
		
		if (initializer.dropTables(context)){
			if (initializer.createTables(context)){
				initializer.loadTestData(context);
			}
		}
    }

    public void contextDestroyed(ServletContextEvent event) {
        // nothing to destroy
    }
	
}
