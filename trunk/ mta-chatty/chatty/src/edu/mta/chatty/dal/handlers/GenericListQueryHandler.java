package edu.mta.chatty.dal.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;

public abstract class GenericListQueryHandler<T> extends ListQueryHandler<T>{
	private final static Logger logger = Logger.getLogger(GenericListQueryHandler.class.getName());
	protected GenericListQueryHandler(List<T> results) {
		super(results);
	}
	
	abstract protected T create();
	
	@Override
	protected T handleResult(ResultSet resultSet) throws SQLException {
		T t = create();
		ResultSetMetaData meta = resultSet.getMetaData();
		for (int i=1;i<=meta.getColumnCount();i++){
			String label = meta.getColumnLabel(i);
			Object value = resultSet.getObject(i);
			try {
				BeanUtils.setProperty(t, label, value);
			} catch (Exception e) {
				String msg = String.format("unable to set %s=%s for class %s with error %s. continue handling other members", label,value,t.getClass().getName(),e);
				logger.info(msg);
				logger.log(Level.INFO, e.getMessage(), e);
			}

		}
		return t;
	}

}
