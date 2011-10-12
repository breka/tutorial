package org.imogene.rcp.core.view;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.part.ViewPart;
import org.imogene.common.data.Synchronizable;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.wrapper.CoreMessages;
import org.imogene.sync.client.EntityListener;

public abstract class IEntityForm extends ViewPart implements ISaveablePart2 {
	
	protected Set<EntityListener> listeners = new HashSet<EntityListener>();

	private IAction saveAction;
	
	private IAction editAction;
	
	private IAction cancelAction;
	
	protected boolean isNew = true;
	
	protected boolean isDirty = true;
	
	protected boolean editable = true;
	
	protected boolean modifiable = true;
	
	private Color errorColor;
	
	public IEntityForm(){
		super();		
		errorColor = new Color(Display.getCurrent(),255, 102, 86);
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Initialize the actions available in this form
	 */
	protected void createAction(){
		
		saveAction = new Action(CoreMessages.getString("form_save"), ImogPlugin.getImageDescriptor("icons/save-16x16.png")){
			public void run(){
				if (validate()) {					
					isDirty = false;
					isNew = false;					
					setEditable(false);
					computeVisibility();
					save();
				}
			}
		};
		editAction = new Action(CoreMessages.getString("form_edit"), ImogPlugin.getImageDescriptor("icons/edit-16x16.gif")){
			public void run(){				
				//isDirty=true;	
				isNew = false;	
				setEditable(true);
				computeVisibility();
			}
		};
		cancelAction = new Action(CoreMessages.getString("form_edit_cancel"), ImogPlugin.getImageDescriptor("icons/cancel-16x16.gif")){
				public void run(){
					if(isNew){
						getSite().getPage().hideView(IEntityForm.this);
					}else{
						cancel();						
						setEditable(false);
						//isDirty = false;
						computeVisibility();
					}
				}
		};
	}	
	
	/**
	 * Initialize this form action bar.
	 */
	protected void initActionBar(){
		IActionBars bars = getViewSite().getActionBars();	
		
		/* display information in the action bar */				
		IToolBarManager toolBar = bars.getToolBarManager();	
		toolBar.add(editAction);
		toolBar.add(cancelAction);
		toolBar.add(saveAction);		
		bars.updateActionBars();		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {		
		//save();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSaveAs() {		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean isDirty() {		
		return isDirty;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean isSaveAsAllowed() {		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean isSaveOnCloseNeeded() {		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public int promptToSaveOnClose() {
		return SWT.DEFAULT;
	}

	/**
	 * Compute the visibility of the action buttons 
	 */
	protected void computeVisibility(){
		if(editable){
			saveAction.setEnabled(true);
			cancelAction.setEnabled(true);
			editAction.setEnabled(false);
		}else{
			saveAction.setEnabled(false);
			cancelAction.setEnabled(false);
			editAction.setEnabled(modifiable);
		}
	}
	
	protected void setLabelErrorColor(Control w, boolean error){
		if(error){
			w.setForeground(errorColor);
		}else{
			w.setForeground(w.getParent().getForeground());
		}
	}
	
	protected void setImageErrorColor(Control w, boolean error){
		if(error){
			w.setBackground(errorColor);
		}else{
			w.setBackground(w.getParent().getBackground());
		}
	}
	
	@Override
	public void dispose() {
		errorColor.dispose();	
		for (EntityListener listener : listeners) {
			removeListener(listener);
		}
		super.dispose();
	}
	
	public synchronized void addListener(EntityListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeListener(EntityListener listener) {
		listeners.remove(listener);
	}

	public abstract void save();
	
	public abstract void cancel();
	
	public abstract void setEditable(boolean editable);
	
	public abstract boolean validate();
	
	public abstract void setInput(Synchronizable entity);
	
	public abstract void initializeForm ();
	
	public abstract void initializeForm (Synchronizable entity);
	
	
}
