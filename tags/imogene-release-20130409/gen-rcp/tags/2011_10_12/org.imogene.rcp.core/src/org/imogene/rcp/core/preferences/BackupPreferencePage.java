package org.imogene.rcp.core.preferences;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.imogene.rcp.core.Constants;
import org.imogene.rcp.core.ImogPlugin;


public class BackupPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Text pathText;
	private String path;
	private boolean pathValid=true;
	
	private boolean isActivated;
	private Button isActivatedButton;
	
	private boolean isEncrypted;
	private Button isEncryptedButton;
	
	
	
	@Override
	protected Control createContents(Composite parent) {
		Composite embedded = new Composite(parent, SWT.NONE);
		embedded.setLayout(new GridLayout());
		createActivationGroup(embedded);
		createEncryptionGroup(embedded);
		createPathGroup(embedded);
		return embedded;
	}

	@Override
	public void init(IWorkbench arg0) {
		IPreferenceStore store = ImogPlugin.getDefault().getPreferenceStore();
		path = store.getString(Constants.KEY_BACKUP_PATH);
		isEncrypted = store.getBoolean(Constants.KEY_BACKUP_ENCRYPTION);
		isActivated = store.getBoolean(Constants.KEY_BACKUP_ACTIVATED);
	}
	
	/**
	 * Group to present the database directory preference
	 * @param parent the parent composite
	 */
	private void createPathGroup(Composite parent){
		/* widget group */
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout(3, false));		
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText("Backup directory");
		/* label and file selector */
		pathText = new Text(group, SWT.BORDER);
		pathText.setText(path);
		pathText.setEditable(false);
		pathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pathText.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent arg0) {				
				if(pathText.getText()==null || pathText.getText().equals(""))
					pathValid=false;
				else
					pathValid=true;
				updateStatus();
			}
		});
		Button recorderButton = new Button(group, SWT.PUSH);
		recorderButton.setText("Select");
		recorderButton.addSelectionListener(new SelectionAdapter(){			
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog fd = new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);				
				String path = fd.open();
				if(path!=null){
					if(!path.endsWith(File.separator))
						path=path+File.separator;
					pathText.setText(path);
				}
			}						
		});
	}
	
	private  void createActivationGroup(Composite parent){
		/* widget group */
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout(2, false));		
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText("Activation");
		/* label and field */
		Label label = new Label(group, SWT.NONE);
		label.setText("Avtivated: ");
		isActivatedButton = new Button(group, SWT.CHECK);
		isActivatedButton.setSelection(isActivated);
	}
	
	private  void createEncryptionGroup(Composite parent){
		/* widget group */
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout(2, false));		
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText("Encrytion");
		/* label and field */
		Label label = new Label(group, SWT.NONE);
		label.setText("Encrypted: ");
		isEncryptedButton = new Button(group, SWT.CHECK);
		isEncryptedButton.setSelection(isEncrypted);
	}
	
		
	/**
	 * Update the status of the preference page
	 */
	private void updateStatus(){
		StringBuffer errorMsg = new StringBuffer();
		if(pathValid){
			setValid(true);
			setErrorMessage(null);
		}else{
			setValid(false);
			errorMsg.append("Invalid database directory");
			setErrorMessage(errorMsg.toString());
		}		
	}

	@Override
	protected void performApply() {
		if(isValid()){
			IPreferenceStore store = ImogPlugin.getDefault().getPreferenceStore();
			store.setValue(Constants.KEY_BACKUP_PATH, pathText.getText());
			store.setValue(Constants.KEY_BACKUP_ACTIVATED, isActivatedButton.getSelection());
			store.setValue(Constants.KEY_BACKUP_ENCRYPTION, isEncryptedButton.getSelection());
		}
	}

	@Override
	public boolean performOk() {
		performApply();
		return super.performOk();
	}

	
	
	
}
