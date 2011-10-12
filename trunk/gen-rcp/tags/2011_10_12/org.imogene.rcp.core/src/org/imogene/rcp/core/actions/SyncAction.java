package org.imogene.rcp.core.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.imogene.rcp.core.view.SyncViewImpl;


public class SyncAction implements IWorkbenchWindowActionDelegate{
	private IWorkbenchWindow window;
	
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void init(IWorkbenchWindow window) {		
		this.window = window;
	}

	public void run(IAction action) {	
		try{			
			/* open the view */
			window.getActivePage().showView(SyncViewImpl.ID);
		}catch(PartInitException ex){
			ex.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

}
