package org.imogene.rcp.core.widget;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.view.IEntityForm;
import org.imogene.rcp.core.wrapper.CoreMessages;

/**
 * Widget to display a relation field with cardinality = 1
 * @author Medes-IMPS
 */
public class RelationCombo extends Composite {

	private Image openIcon = ImogPlugin.getImageDescriptor("icons/info.png").createImage();
	
	private Image refreshIcon = ImogPlugin.getImageDescriptor("icons/reload_page.png").createImage();
	
	private Image addIcon;
	
	private Combo entityList;
	
	private Label openLabel;
	
	private Label refreshLabel;
	
	private Label addLabel;
	
	@SuppressWarnings("unused")
	private FormToolkit toolkit;
	
	private EntityHandler handler;
	
	private String selectedId;
	
	private String formID = null;
	
	
	/**
	 * Create a relation combo for the standard style
	 * @param parent the parent composite
	 */
	public RelationCombo(Composite parent, String className){
		this(parent, false, className, null);			
	}
	
	/**
	 * Create a relation combo
	 * @param parent the parent composite
	 * @param formStyle true to set the HTMLForm style
	 */
	public RelationCombo(Composite parent, boolean formStyle, String className){
		this(parent, formStyle, className, null);
	}
	
	/**
	 * Create a relation combo for the standard style
	 * @param parent the parent composite
	 */
	public RelationCombo(Composite parent, String className, String formId){
		this(parent, false, className, formId);	
	}
	
	/**
	 * Create a relation combo
	 * @param parent the parent composite
	 * @param formStyle true to set the HTMLForm style
	 */
	public RelationCombo(Composite parent, boolean formStyle, String className, String formId){
		super(parent, SWT.NONE);
		formID = formId;
		
		handler = ImogPlugin.getDefault().getDataHandlerManager().getHandler(className);

		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		setLayout(layout);
		
		entityList = new Combo(this, SWT.SINGLE | SWT.READ_ONLY);
		entityList.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		entityList.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedId = (String)entityList.getData(String.valueOf(entityList.getSelectionIndex()));
				super.widgetSelected(e);
			}
			
		});	
		
		refreshLabel = new Label(this, SWT.NONE);
		refreshLabel.setImage(refreshIcon);
		refreshLabel.setToolTipText(CoreMessages.getString("tooltip_refresh"));
		refreshLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		refreshLabel.addMouseListener(new MouseAdapter(){
		   @Override
		   public void mouseUp(MouseEvent e) {    
			   fill();    
		   }
		});
		
		openLabel = new Label(this, SWT.NONE);
		openLabel.setImage(openIcon);
		openLabel.setToolTipText(CoreMessages.getString("tooltip_view"));
		openLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		openLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseUp(MouseEvent e) {
				if(formID != null && getSelected()!=null){
					try {
						IWorkbenchPage page = ImogPlugin.getDefault()
								.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage();
						IViewPart lview = page.showView(formID, getSelected()
								.getId(), IWorkbenchPage.VIEW_ACTIVATE);
						((IEntityForm) lview).setInput(getSelected());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		if(formID==null){
			openLabel.setVisible(false);
		}
		if(formStyle){
			createFormWidgets();
		}
	}
	
	/**
	 * create the widgets with a standard style
	 */
	@SuppressWarnings("unused")
	private void createStandardWidgets(){
		
	}
	
	/**
	 * Create widget with the form style
	 */
	private void createFormWidgets(){
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		openLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		refreshLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
	}
	
	/**
	 * Fill the combo list with entities presents in the database
	 * @param className entities class name
	 */
	public void fill(){
		int lastIndex = entityList.getSelectionIndex();
		List<Synchronizable> entities = handler.loadEntities(null);
		entityList.removeAll();
		int index=0;
		for(Synchronizable entity:entities){
			entityList.add(entity.getDisplayValue());
			entityList.setData(String.valueOf(index), entity.getId());
			index++;
		}		
		entityList.select(lastIndex);
	}
	
	/**
	 * Adds an entity to the combo 
	 * @param entity the entity to be added
	 */
	public void add(Synchronizable entity) {
		entityList.add(entity.getDisplayValue());
		entityList.setData(String.valueOf(entityList.getItemCount()-1), entity.getId());
	}
	
	/**
	 * Get the selected entity
	 * @return the selected entity
	 */
	public Synchronizable getSelected(){
		int index = entityList.getSelectionIndex();
		String id = (String)entityList.getData(String.valueOf(index));
		return handler.loadEntity(id, null);
	}
	
	/**
	 * Select the item that match the specified entity
	 * @param entity the entity selected
	 */
	public void select(Synchronizable entity){
		selectedId = entity.getId();
		select(selectedId);
	}
	
	/**
	 * Select the item that match this entity id
	 */
	private void select(String id){
		if (id != null) {
			for (int i = 0; i < entityList.getItemCount(); i++) {
				if (id.equals(entityList.getData(String.valueOf(i)))) {					
					entityList.select(i);
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.rcp.data.EntitiesListener#entitiesChanged()
	 */
	public void entitiesChanged() {		
		fill();		
		select(selectedId);
	}
	
	@Override
	public void dispose() {
		openIcon.dispose();
		refreshIcon.dispose();
		if (addLabel!=null)
			addIcon.dispose();
		super.dispose();
	}
	
	/**
	  * Add a selection listener
	  * @param listener the listener to add
	  */
	 public void addSelectionListener(SelectionListener listener){
		 entityList.addSelectionListener(listener);
	 }

	 /**
	  * Remove a selection listener
	  * @param listener the listener to remove
	  */
	 public void removeSelectionListener(SelectionListener listener){
		 entityList.removeSelectionListener(listener);
	 }

	 @Override
	 public boolean isFocusControl() {  
		 return (entityList.isFocusControl() || super.isFocusControl());
	 }

	@Override
	public void setEnabled(boolean enabled) {
		entityList.setEnabled(enabled);
		refreshLabel.setVisible(enabled);
		if (addLabel!=null)
			addLabel.setVisible(enabled);
	}
	
	
	/**
	 * Add a button to be able to create a related entity
	 * @param listener
	 */
	public void addCreateRelationEntityButton(MouseListener listener) {
		
		addLabel = new Label(this, SWT.NONE);
		addLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		addIcon = ImogPlugin.getImageDescriptor("icons/edit_add.png").createImage();
		addLabel.setImage(addIcon);
		addLabel.setToolTipText(CoreMessages.getString("tooltip_create_related"));
		addLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		
		addLabel.addMouseListener(listener);				
	}
		
}
