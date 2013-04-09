package org.imogene.rcp.core;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.imogene.rcp.core.wrapper.CoreMessages;


public class View extends ViewPart {
	
	public static final String ID = "Imogene.view";

	 private FormToolkit toolkit;
	 
	 private Form form;	 

	/**
	 * This is a call back that will allow
	 * us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {										
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setText(CoreMessages.getString("core_welcome"));		
		Composite body = form.getBody();
		GridLayout bodyLayout = new GridLayout();
		bodyLayout.marginLeft=50;
		bodyLayout.marginRight=50;
		body.setLayout(bodyLayout);	
		
	}
	
	

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		
	}
}