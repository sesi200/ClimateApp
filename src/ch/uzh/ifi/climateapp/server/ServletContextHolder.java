package ch.uzh.ifi.climateapp.server;

import javax.servlet.ServletContext;

/**
 * This class is a singleton class which has only one task of holding the
 * context of the application after the point when it has been created to allow
 * easy access to the context.
 * 
 * @author lada
 *
 */
public class ServletContextHolder {

	private static ServletContextHolder instance;
	public static ServletContext CONTEXT;

	private ServletContextHolder(ServletContext context) {
		CONTEXT = context;
	}

	public static ServletContextHolder getInstance(ServletContext context) {
		if (instance == null) {
			instance = new ServletContextHolder(context);
		}
		return instance;
	}
}
