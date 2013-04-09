package org.imogene.rcp.core.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.imogene.rcp.core.wrapper.CoreMessages;


public class DevicesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	@Override
	protected Control createContents(Composite parent) {
		
		super.setTitle(CoreMessages.getString("device_text"));
		
		Label description = new Label(parent, SWT.NONE);
		description.setText(CoreMessages.getString("device_desc"));
		
		return description;
	}

	public void init(IWorkbench workbench) {		
		
	}
	

}
