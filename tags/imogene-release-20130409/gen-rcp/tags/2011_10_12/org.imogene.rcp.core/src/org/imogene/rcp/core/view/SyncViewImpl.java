package org.imogene.rcp.core.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.imogene.common.data.EntitiesListener;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.sync.monitor.SyncMonitorPart;
import org.imogene.rcp.core.tools.DateHelper;
import org.imogene.rcp.core.wrapper.CoreMessages;
import org.imogene.sync.client.dao.sqlite.HistoryDaoHibernate;
import org.imogene.sync.client.history.SyncHistory;

public class SyncViewImpl extends ViewPart implements EntitiesListener, SyncView {

	public static String ID = "Imogene.SyncView";		
	
	private Composite historyList;
	
	private Composite content; 
	
	private Composite messages;
	
	private HistoryDaoHibernate dao;
	
	private Label dateLabel;
	
	private Label dateFormat;
		
	
	@Override
	public void createPartControl(Composite parent) {
		dao = new HistoryDaoHibernate();		
		super.setPartName(CoreMessages.getString("sync_monitor_view"));		
		parent.setLayout(new FillLayout());		
		content = new Composite(parent, SWT.NONE);
		content.setLayout(new GridLayout());
		dateLabel = new Label(content,SWT.NONE);	
		dateLabel.setText(CoreMessages.getString("sync_view_last_date"));
		dateFormat = new Label(content, SWT.NONE);
		new SyncMonitorPart(content, this);	
		createMessageComposite();		
		updateDate();
	}

	@Override
	public void setFocus() {
		
	}
		
	
	private void createMessageComposite(){
		messages = new Composite(content, SWT.NONE);
		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.grabExcessVerticalSpace = true;
		messages.setLayoutData(gData);
		messages.setLayout(new GridLayout());				
	}
	
	/**
	 * clear the message panel
	 */
	public void clearMessage(){
		messages.dispose();
		createMessageComposite();
	}
	
	/**
	 * Add information message.
	 * @param message informations message.
	 * @param type message icon.
	 */
	public void addMessage(String message, int type){		
		Composite m = new Composite(messages, SWT.NONE);
		GridData mData = new GridData(GridData.FILL_HORIZONTAL);
		m.setLayoutData(mData);
		m.setLayout(new GridLayout(2, false));
		Label imageLabel = new Label(m, SWT.NONE);
		final Image img;
		if (type == 1)
			img = ImogPlugin.getImageDescriptor("icons/ok_status.png").createImage();
		else
			img = ImogPlugin.getImageDescriptor("icons/error_status.png").createImage();
		imageLabel.setImage(img);
		messages.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				img.dispose();
			}
		});
		Label textLabel = new Label(m, SWT.NONE);
		textLabel.setText(message);
		messages.layout();
		content.layout();
	}
	
	/**
	 * update the label with the last ok sync date
	 */
	public void updateDate(){
		SyncHistory h = dao.loadLastOk();		
		if(h!= null && h.getDate()!=null)
			dateFormat.setText(DateHelper.toStringDateTime(h.getDate()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.rcp.common.data.EntitiesListener#entitiesChanged()
	 */
	public void entitiesChanged() {	
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				historyList.dispose();			
				content.setSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));		
			}
			});
	}
	
	

	
}
