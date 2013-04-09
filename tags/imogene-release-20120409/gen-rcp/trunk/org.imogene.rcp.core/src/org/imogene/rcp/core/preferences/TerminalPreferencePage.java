package org.imogene.rcp.core.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.wrapper.CoreMessages;


public class TerminalPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private String id;
	
	@Override
	protected Control createContents(Composite parent) {	
		
		super.setTitle(CoreMessages.getString("terminal_text"));
		
		Composite embedded = new Composite(parent, SWT.NONE);		
		embedded.setLayout(new GridLayout());	
		
		/* group terminal id */
		Group idGroup = new Group(embedded, SWT.NONE);
		idGroup.setLayout(new GridLayout(2, false));
		idGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		idGroup.setText(CoreMessages.getString("terminal_group_label"));
		
		/* field terminal id */
		Label terminalIdLabel = new Label(idGroup, SWT.NONE);
		terminalIdLabel.setText(CoreMessages.getString("terminal_field_label") + ": ");		
		Text terminalIdText = new Text(idGroup, SWT.BORDER);
		terminalIdText.setData(new GridData(GridData.FILL_HORIZONTAL));
		terminalIdText.setText(id);
		terminalIdText.setEditable(false);
		return embedded;
	}

	public void init(IWorkbench workbench) {		
		id = ImogPlugin.getDefault().getPreferenceStore().getString("TERMINAL_ID");
	}

	
}
