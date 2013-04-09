package org.imogene.rcp.core;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);				
		layout.addStandaloneView(View.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
		layout.setFixed(true);			
	}
		

}
