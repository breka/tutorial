package org.imogene.rcp.core;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;

public interface CloseValidator {

	public boolean check();
	
	public void init(Display display);
	
	public void setLast(IWorkbenchWindow window);
	
}
