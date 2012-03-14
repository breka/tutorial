package org.imogene.rcp.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.imogene.rcp.core.wrapper.CoreMessages;


public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "Imogene.perspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		configurer.getWorkbenchConfigurer().setSaveAndRestore(true);
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		
		IExtensionRegistry registry = Platform.getExtensionRegistry();		
		IExtensionPoint extensionPoint = registry.getExtensionPoint("org.imogene.rcp.core", "Modules");
		
		IExtension[] extensions = extensionPoint.getExtensions();
		if(extensions.length>0){
			IConfigurationElement[] elements = extensions[0].getConfigurationElements();
			return elements[0].getAttribute("perspective");			
		}		
				
		return PERSPECTIVE_ID;
	}

	@Override
	public boolean preShutdown() {
		boolean result = super.preShutdown();		
		boolean response = MessageDialog.openQuestion(
				Display.getDefault().getActiveShell(), CoreMessages
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
						validator.check();
					} catch (CoreException ce) {
						ce.printStackTrace();
					}
				}						
			}				
		}		
		return result && response;
	}
		

}
