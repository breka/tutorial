package org.imogene.rcp.core.widget.field.clientfilter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.wrapper.CoreMessages;


/**
 * NOT FINALIZED YET
 * Widget to display a relation field with cardinality = n
 * @author Medes-IMPS
 */
public class ClientFilterRelationCombo extends Composite {
	
	//private Set<Synchronizable> comboEntities = new HashSet<Synchronizable>();
	private Set<String> comboEntities = new HashSet<String>();
	
	private FormToolkit toolkit;
	
	private boolean formStyle;
	
	private Composite listComposite;
	
	private Label addLabel;
	private Image addIcon;
	
		
	public ClientFilterRelationCombo(Composite parent){		
		this(parent, true);
	}
		
	/**
	 * Create a MultiRelationCombo
	 * @param parent the parent composite
	 * @param pFormId id of the type of form corresponding to the entities listed in the combo
	 * @param pFormStyle true to set the HTMLForm style
	 */
	public ClientFilterRelationCombo(Composite parent, boolean pFormStyle){		
		super(parent, SWT.NONE);
		formStyle = pFormStyle;
		
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.horizontalSpacing = 2;
		setLayout(layout);	
		
		ScrolledComposite sc = new ScrolledComposite(this, SWT.BORDER | SWT.V_SCROLL);
		sc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		GridData scData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
		//scData.heightHint=40;
		sc.setLayoutData(scData);

	    sc.setExpandVertical(true);
	    sc.setExpandHorizontal(true);
		
		if(formStyle){
			toolkit = new FormToolkit(Display.getCurrent());
			setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));	
			listComposite = toolkit.createComposite(sc, SWT.NONE);	
		}
		else {
			listComposite = new Composite(sc, SWT.NONE);
		}
		//listComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		listComposite.setLayout(new GridLayout());
		listComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));	
		
		sc.setContent(listComposite);
	}
	
	/**
	 * Fills the combo with a list of entities
	 * @param entities entity list to fill the combo
	 */
	public void setInput(Map<String, String> entities) {	
		
		if (entities!=null) {
						
			if (entities.size()>5) {
				((GridData)listComposite.getParent().getLayoutData()).heightHint=80;
				((ScrolledComposite)listComposite.getParent()).setMinHeight(500);
			}
			
			// empty list if already filled
			if (listComposite.getChildren().length>0) {
				comboEntities.clear();
				for (Control control: listComposite.getChildren()) {
					control.dispose();
				}
			}
			
			// fill list
			for (Entry<String,String> entry:entities.entrySet()) {
				add((String)entry.getKey(), (String)entry.getValue());
			}
		}
	}
	
	/**
	 * Adds an entity to the combo 
	 * @param entity the entity to be added to the combo
	 */
	public void add(final String entityId, String displayValue) {
		
		comboEntities.add(entityId);
		
		Label label;
		if(formStyle){
			label = toolkit.createLabel(listComposite, displayValue, SWT.WRAP);
		}
		else {
			label = new Label(listComposite, SWT.WRAP);
			label.setText(displayValue);
		}
		getParent().layout(true,true);
	}
	
	/**
	 * Get the selected entities
	 * @return the selected entities
	 */
	public Set<String> getSelected(){
		return comboEntities;
	}
	
	/**
	 * Add a button to be able to create a related entity
	 * @param listener
	 */
	public void addAssignRelationEntityButton(MouseListener listener) {
		
		addLabel = new Label(this, SWT.NONE);
		addLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		addIcon = ImogPlugin.getImageDescriptor("icons/edit_add.png").createImage();
		addLabel.setImage(addIcon);
		addLabel.setToolTipText(CoreMessages.getString("tooltip_create_related"));
		addLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		
		addLabel.addMouseListener(listener);				
	}
	
	@Override
	public void dispose() {
		if (addLabel!=null)
			addIcon.dispose();
		super.dispose();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if (addLabel!=null)
			addLabel.setVisible(enabled);
	}

}
