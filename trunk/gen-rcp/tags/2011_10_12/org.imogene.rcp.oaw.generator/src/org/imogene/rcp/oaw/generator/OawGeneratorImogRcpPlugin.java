//------------------------------------------------
//$Id: OawGeneratorSqlInsertPlugin.java,v 1.1 2007-01-24 17:04:55 ylapeyre Exp $
//Medany Project - Medes - Anyware Technologies
//(c) Anyware Technologies 2006 www.anyware-tech.com
//------------------------------------------------

package org.imogene.rcp.oaw.generator;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class OawGeneratorImogRcpPlugin extends AbstractUIPlugin {

	// The shared instance.
	private static OawGeneratorImogRcpPlugin plugin;

	/**
	 * The constructor.
	 */
	public OawGeneratorImogRcpPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static OawGeneratorImogRcpPlugin getDefault() {
		return plugin;
	}

	/**
	 * Log a message with given level into the Eclipse log file
	 * 
	 * @param message
	 *            the message to log
	 * @param level
	 *            the message priority
	 */
	public static void log(String message, int level) {
		IStatus status = null;
		status = new Status(level, getId(), IStatus.OK, message, null);
		log(status);
	}

	/**
	 * Log an exception into the Eclipse log file
	 * 
	 * @param e
	 *            the exception to log
	 */
	public static void log(Throwable e) {
		if (e instanceof InvocationTargetException)
			e = ((InvocationTargetException) e).getTargetException();

		IStatus status = null;
		if (e instanceof CoreException)
			status = ((CoreException) e).getStatus();
		else
			status = new Status(IStatus.ERROR, getId(), IStatus.OK, "Error", e);

		log(status);
	}

	/**
	 * Log an IStatus
	 * 
	 * @param status
	 */
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	/**
	 * @return the Plugin Id
	 */
	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
	}
}