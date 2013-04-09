package org.imogene.rcp.core;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.imogene.rcp.core.wrapper.CoreMessages;


public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(getDisplaySize());
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);	
		configurer.setTitle(CoreMessages.getString("core_imog"));
	}				
	
	

	/*@Override
	public boolean preWindowShellClose() {
		boolean result = super.preWindowShellClose();
		
		IWorkbenchWindow window = this.getWindowConfigurer().getWindow();
		
		boolean response = MessageDialog.openQuestion(
				window.getShell(), CoreMessages
						.getString("application_exit_title"),
				CoreMessages.getString("application_exit_text"));
		if (response) {					
			IExtensionRegistry registry = Platform
					.getExtensionRegistry();
			IExtensionPoint closePointsExt = registry
					.getExtensionPoint("org.imogene.rcp.core", "CloseValidator");
			IExtension[] closePoints = closePointsExt.getExtensions();
			for (IExtension closePoint : closePoints) {
				IConfigurationElement[] elements = closePoint
						.getConfigurationElements();
				for(int i=0; i<elements.length; i++){
					IConfigurationElement element = elements[i];
					try {
						CloseValidator validator = (CloseValidator) element
								.createExecutableExtension("Class");
						validator.init(Display.getCurrent());
						if(i==elements.length-1){
							validator.setLast(window);							
						}
						validator.check();
					} catch (CoreException ce) {
						ce.printStackTrace();
					}
				}						
			}				
		}
		return result && response;
	}*/

	private Point getDisplaySize() {
		try {
			Display display = Display.getCurrent();
			Monitor monitor = display.getPrimaryMonitor();
			Rectangle rect = monitor.getBounds();
			return new Point(rect.width, rect.width);
		} catch (Throwable ignore) {
			return new Point(800, 400);
		}
	}
}
