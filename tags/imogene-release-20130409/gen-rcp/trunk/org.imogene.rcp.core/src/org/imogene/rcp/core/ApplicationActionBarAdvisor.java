package org.imogene.rcp.core;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.imogene.rcp.core.wrapper.CoreMessages;


/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {	
	
	private IWorkbenchAction preferencesAction;
	
	private IAction quitAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {

		quitAction = ActionFactory.QUIT.create(window);
		quitAction.setText(CoreMessages.getString("application_exit_menu"));
		quitAction.setImageDescriptor(ImogPlugin
				.getImageDescriptor("icons/exit-16x16.png"));
		preferencesAction = ActionFactory.PREFERENCES.create(window);
		preferencesAction.setImageDescriptor(ImogPlugin
				.getImageDescriptor("icons/preferences-16x16.png"));
		preferencesAction.setText(CoreMessages
				.getString("application_preferences_menu"));
		register(quitAction);
		register(preferencesAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {		
		MenuManager fileMenu = new MenuManager(CoreMessages.getString("application_file_menu"),
				IWorkbenchActionConstants.M_FILE);		
		menuBar.add(fileMenu);			
		fileMenu.add(preferencesAction);
		fileMenu.add(quitAction);		
	}

}
