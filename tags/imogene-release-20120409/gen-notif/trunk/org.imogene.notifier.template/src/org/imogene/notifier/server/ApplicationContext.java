package org.imogene.notifier.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * This class permits to access to the Spring 
 * application context file bean definitions.
 *@author Medes-IMPS
 */
public class ApplicationContext {

	private static Logger logger = Logger.getLogger("org.imogene.notifier");

	/**
	 * The stateless configuration file
	 */
	private static final String STATEFULL_CONTEXT_FILE = "org/imogene/notifier/server/applicationContext.xml";

	/**
	 * Unique instance of the singleton
	 */
	private static ApplicationContext instance;

	/**
	 * Spring context
	 */
	protected GenericApplicationContext springContext;

	/**
	 * @return the unique of the instance
	 */
	public static synchronized final ApplicationContext getInstance() {
		if (instance == null) {
			instance = new ApplicationContext();
			logger.debug("Application context created");
		}
		return instance;
	}

	/**
	 * Constructor
	 */
	protected ApplicationContext() {
		initContextFile();
	}

	/**
	 * Get a bean from its name
	 */
	public Object getBean(String beanName) {
		return springContext.getBean(beanName);
	}

	/**
	 * Init Spring context
	 */
	private void initContextFile() {
		springContext = new GenericApplicationContext();
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(
				springContext);
		xmlReader.loadBeanDefinitions(new ClassPathResource(
				STATEFULL_CONTEXT_FILE));
		springContext.refresh();
	}
}
