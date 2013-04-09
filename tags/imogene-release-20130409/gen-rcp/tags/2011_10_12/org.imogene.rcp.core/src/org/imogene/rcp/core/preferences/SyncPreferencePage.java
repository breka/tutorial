package org.imogene.rcp.core.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.wrapper.CoreMessages;


public class SyncPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private final int DEFAULT_PERIOD = 30000;
	
	private Text urlText;
	private String url;
	private Button bidirectionnal;
	private Button sendOnly;
	private Button receiveOnly;
	private Button automatic;
	private Button manual;
	private Text periodText;
	private int syncType;
	private boolean loop;
	private int period;
	
	@Override
	protected Control createContents(Composite parent) {
		
		super.setTitle(CoreMessages.getString("sync_text"));
		
		Composite embedded = new Composite(parent, SWT.NONE);
		embedded.setLayout(new GridLayout());
		createUrlGroup(embedded);
		createLoopGroup(embedded);
		createTypeGroup(embedded);
		return embedded;
	}

	
	private void createUrlGroup(Composite parent){
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout(2, false));		
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText(CoreMessages.getString("sync_group_server"));
		Label urlLabel = new Label(group, SWT.NONE);		
		urlLabel.setText(CoreMessages.getString("sync_field_url") + ": ");
		urlText = new Text(group, SWT.BORDER);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		urlText.setText(url);
		
	}
	
	private void createLoopGroup(Composite parent){
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout(3, false));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText(CoreMessages.getString("sync_group_way"));		
		manual = new Button(group, SWT.RADIO);
		manual.setText(CoreMessages.getString("sync_way_manual"));
		manual.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(((Button)e.widget).getSelection())
					periodText.setEnabled(false);
			}
		});
		automatic = new Button(group, SWT.RADIO);
		automatic.setText(CoreMessages.getString("sync_way_auto"));
		automatic.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(((Button)e.widget).getSelection())
					periodText.setEnabled(true);
			}
		});
		periodText = new Text(group, SWT.BORDER);
		if(loop){
			automatic.setSelection(true);
			periodText.setEnabled(true);
		}else{
			manual.setSelection(true);
			periodText.setEnabled(false);
		}
		if(period>0){
			periodText.setText(String.valueOf(period));			
		}else{
			periodText.setText(String.valueOf(DEFAULT_PERIOD));
		}
	}
	
	private void createTypeGroup(Composite parent){
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText(CoreMessages.getString("sync_group_type"));
		bidirectionnal = new Button(group, SWT.RADIO);
		bidirectionnal.setText(CoreMessages.getString("sync_bidirectionnal"));
		if(syncType==0) bidirectionnal.setSelection(true);
		sendOnly = new Button(group, SWT.RADIO);
		sendOnly.setText(CoreMessages.getString("sync_sendonly"));
		if(syncType==1) sendOnly.setSelection(true);
		receiveOnly = new Button(group, SWT.RADIO);
		receiveOnly.setText(CoreMessages.getString("sync_receiveonly"));
		if(syncType==2) receiveOnly.setSelection(true);
		group.setEnabled(false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		url = ImogPlugin.getDefault().getPreferenceStore().getString("SYNC_URL");
		if(url==null || url.equals("")){
			url = "http://localhost:8080/ImogeneSynchro";
			ImogPlugin.getDefault().getPreferenceStore().setValue("SYNC_URL", url);
		}		
		syncType = ImogPlugin.getDefault().getPreferenceStore().getInt("SYNC_TYPE");
		loop = ImogPlugin.getDefault().getPreferenceStore().getBoolean("SYNC_LOOP");
		period = ImogPlugin.getDefault().getPreferenceStore().getInt("SYNC_PERIOD");		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#isValid()
	 */
	public boolean isValid() {
		
		return super.isValid();		
	}
	
	@Override
	protected void performApply() {		
		if(isValid()){			
			ImogPlugin.getDefault().getPreferenceStore().putValue("SYNC_URL", urlText.getText());
			if(bidirectionnal.getSelection())
				syncType = 0;
			if(sendOnly.getSelection())
				syncType = 1;
			if(receiveOnly.getSelection())
				syncType = 2;
			
			if(automatic.getSelection()){
				loop=true;				
			}else{
				loop = false;						
			}
			try{
				period = Integer.parseInt(periodText.getText());
			}catch(NumberFormatException ex){
				period = DEFAULT_PERIOD;
			}
			ImogPlugin.getDefault().getPreferenceStore().setValue("SYNC_PERIOD", period);	
			ImogPlugin.getDefault().getPreferenceStore().setValue("SYNC_LOOP", loop);
			ImogPlugin.getDefault().getPreferenceStore().setValue("SYNC_TYPE", syncType);		
			ImogPlugin.getDefault().restartServer(loop, period);
		}		
	}

	@Override
	public boolean performOk() {
		if(isValid()){
			performApply();
			return true;
		}
		return false;
	}
	
}
