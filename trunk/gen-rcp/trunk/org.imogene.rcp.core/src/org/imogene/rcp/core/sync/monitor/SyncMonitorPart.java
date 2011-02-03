package org.imogene.rcp.core.sync.monitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.view.SyncView;
import org.imogene.rcp.core.wrapper.CoreMessages;
import org.imogene.sync.client.OptimizedSynchronizer;
import org.imogene.sync.client.SyncListener;

/**
 * This composite monitor a SyncJob. It displays a progress bar, an info button
 * that display details about the running job and a cancel button to cancel the
 * running job.
 * 
 * @author MEDES-IMPS
 */
public class SyncMonitorPart extends Composite implements SyncListener {

	private static final int STATUS_INIT = 0;

	private static final int STATUS_RESUM_REC = 1;

	private static final int STATUS_RESUM_SEND = 2;

	private static final int STATUS_REC = 3;

	private static final int STATUS_SEND = 4;

	@SuppressWarnings("unused")
	private static final int STATUS_ERROR = 5;

	@SuppressWarnings("unused")
	private static final int FINISHED = 6;

	/* embedded <code>ProgressMonitorPart</code> */
	private ProgressMonitorPart monitorPart;

	private Set<SyncEndProcessListener> listeners = new HashSet<SyncEndProcessListener>();

	private OptimizedSynchronizer s;

	/* the run button */
	private Button runButton;

	private SyncView view;

	/* progress messages */
	private Label message;

	private int status = -1;

	private boolean manual = true;

	public SyncMonitorPart(Composite parent, SyncView pView, boolean pManual) {
		super(parent, SWT.NONE);
		manual = pManual;
		setBackgroundMode(SWT.INHERIT_FORCE);
		view = pView;
		setLayout(new GridLayout(2, false));
		setWidgets();
	}

	/**
	 * Widget that monitor a collect process.
	 * 
	 * @param parent
	 *            the parent composite
	 */
	public SyncMonitorPart(Composite parent, SyncView pView) {
		this(parent, pView, true);
	}

	/**
	 * Set widgets embedded in this composite
	 */
	private void setWidgets() {
		/* run button */
		runButton = new Button(this, SWT.PUSH);
		runButton.setImage(ImogPlugin
				.getImageDescriptor("icons/run-16x16.png").createImage());
		runButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_END));
		runButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				runProcess();
			}

		});
		if (!manual)
			runButton.setVisible(false);
		/* monitor part */
		monitorPart = new ProgressMonitorPart(this, new GridLayout());
		GridData mData = new GridData(GridData.FILL_HORIZONTAL);
		monitorPart.setLayoutData(mData);
		monitorPart.beginTask(CoreMessages.getString("job_sync_inactiv"), 10);
		/* message part */
		message = new Label(this, SWT.NONE);
		GridData messageGd = new GridData(GridData.FILL_HORIZONTAL);
		messageGd.horizontalSpan = 3;
		message.setLayoutData(messageGd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.imogene.rcp.sync.client.SyncListener#finish()
	 */
	public void finish() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				monitorPart.worked(10);
				monitorPart.done();
				monitorPart.beginTask(CoreMessages
						.getString("job_sync_inactiv"), 10);
				message.setText("");
				runButton.setEnabled(true);
				s.removeListener(SyncMonitorPart.this);
				if (status == STATUS_REC || status == STATUS_RESUM_REC)
					view.addMessage(CoreMessages
							.getString("job_sync_reception"), 1);
				view.addMessage(CoreMessages.getString("job_sync_end"), 1);
				view.updateDate();
				notifyListeners(1);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.imogene.rcp.sync.client.SyncListener#initSession(java.lang.String)
	 */
	public void initSession(String id) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				monitorPart.beginTask(CoreMessages
						.getString("job_sync_init_session"), 10);
				runButton.setEnabled(false);
				status = STATUS_INIT;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.imogene.rcp.sync.client.SyncListener#receiving(int)
	 */
	public void receiving(int bytesToReceive) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				message.setText(CoreMessages.getString("job_sync_receiving"));
				monitorPart.worked(5);
				status = STATUS_REC;
				view.addMessage(CoreMessages.getString("job_sync_transmit"), 1);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.imogene.rcp.sync.client.SyncListener#resumeReceive(int, int)
	 */
	public void resumeReceive(int bytesToReceive, int allBytes) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				message.setText(CoreMessages.getString("job_sync_recep_retry"));
				monitorPart.worked(5);
				status = STATUS_RESUM_REC;
				view.addMessage(CoreMessages
						.getString("job_sync_recep_retry_title"), 1);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.imogene.rcp.sync.client.SyncListener#resumeSend(int, int)
	 */
	public void resumeSend(int bytesToSend, int allBytes) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				message.setText(CoreMessages.getString("job_sync_send_retry"));
				monitorPart.worked(2);
				status = STATUS_RESUM_SEND;
				view.addMessage(CoreMessages
						.getString("job_sync_send_retry_title"), 1);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.imogene.rcp.sync.client.SyncListener#sending(int)
	 */
	public void sending(int bytesToSend) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				message.setText(CoreMessages.getString("job_sync_sending"));
				monitorPart.worked(2);
				status = STATUS_SEND;
				view.addMessage(CoreMessages.getString("job_sync_init"), 1);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.imogene.rcp.sync.client.SyncListener#syncError(int)
	 */
	public void syncError(int code) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				message.setText(CoreMessages.getString("job_sync_error"));
				monitorPart.done();
				monitorPart.setTaskName("Inactif");
				if (status == -1) {
					view.addMessage(CoreMessages.getString("job_sync_init"), 2);
					message.setText("Erreur lors de l'initialisation.");
				}
				if (status == STATUS_INIT) {
					view.addMessage(CoreMessages.getString("job_sync_init"), 2);
					message.setText("Erreur lors de l'initialisation.");
				}
				if (status == STATUS_REC) {
					view.addMessage(CoreMessages
							.getString("job_sync_reception"), 2);
					message.setText("Erreur lors de la r\u00E9ception.");
				}
				if (status == STATUS_SEND) {
					view.addMessage(
							CoreMessages.getString("job_sync_transmit"), 2);
					message.setText("Erreur lors de l'\u00E9mission.");
				}
				if (status == STATUS_RESUM_SEND) {
					view.addMessage(CoreMessages
							.getString("job_sync_send_retry_title"), 2);
					message
							.setText("Erreur lors de la reprise de l'\u00E9mission.");
				}
				if (status == STATUS_RESUM_REC) {
					view.addMessage(CoreMessages
							.getString("job_sync_recep_retry_title"), 2);
					message
							.setText("Erreur lors de la reprise de la r\u00E9ception.");
				}
				notifyListeners(0);
				runButton.setEnabled(true);
			}
		});

	}

	public void externalRun() {
		runProcess();
	}

	private void runProcess() {
		if (ImogPlugin.getDefault().getPreferenceStore().getBoolean(
				"SYNC_LOOP")) {
			MessageDialog.openInformation(
					Display.getCurrent().getActiveShell(), CoreMessages
							.getString("popup_info"), CoreMessages
							.getString("job_sync_auto"));
		} else {
			view.clearMessage();
			s = ImogPlugin.getDefault().startSynchro();
			s.addSyncListener(SyncMonitorPart.this);
			// runButton.setEnabled(false);
			status = -1;
		}
	}

	/**
	 * 
	 * @param listener
	 */
	public void addEndProcessListener(SyncEndProcessListener listener) {
		listeners.add(listener);
	}

	/**
	 * 
	 * @param listener
	 */
	public void removeEndProcessListener(SyncEndProcessListener listener) {
		listeners.add(listener);
	}

	private void notifyListeners(int status) {
		for (SyncEndProcessListener listener : listeners)
			listener.syncProcessEnd(status);
	}

}
