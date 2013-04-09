package org.imogene.web.gwt.client.ui.form;

import org.imogene.web.gwt.client.LocalSession;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.i18n.TextFormatUtil;
import org.imogene.web.gwt.client.ui.panel.TaskWrapperPanel;
import org.imogene.web.gwt.common.entity.ImogBean;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;




/**
 * This class enables to display an entity form,
 * with common buttons to edit and save it, where the form data
 * are organized as sections in a FlexTable.
 * @author Medes-IMPS
 */
public abstract class AbstractFormComposite extends DisclosureContainerComposite {
	
	/* constants */
	private static final int MAX_COL = 2;
	
	/* status */
	protected String title;
	protected boolean isNew;	
	protected boolean isModifiable = true;	
	protected boolean isEdited = false;	
	protected boolean closeable = true;	
	protected boolean isDirty;	
	private String color;	

	/* widgets */
	protected TaskWrapperPanel container = null;
	private VerticalPanel layout;
	private VerticalPanel metaInfoPanel;
	
	/* form metadata */
	private HTML idLabel;
	private HTML creationLabel;		
	private HTML modificationLabel;
	
	/* true if field groups to be displayed in tabulations */
	private boolean withTabs = false;
	/* for forms with group fields in one page */
	private FlexTable contentTable;
	/* for forms with group fields in tabulations */
	private TabPanel tabPanel;
		
	/* form action buttons */
	protected PushButton saveButton;	
	protected PushButton editButton;	
	protected PushButton cancelButton;
	protected PushButton closeButton;
	protected PushButton printButton;
	
	
	public AbstractFormComposite(String pTitle, String pColor){
		super(pTitle, pColor);
		title = pTitle;
		color = pColor;
		layout();
		properties();
		behavior();
	}
	
	public AbstractFormComposite(String pTitle, String pColor, boolean withTabs){
		super(pTitle, pColor);
		this.withTabs = withTabs;
		title = pTitle;
		color = pColor;
		layout();
		properties();
		behavior();
	}
	
	/** form layout */
	private void layout(){
		layout = new VerticalPanel();
		layoutButtonsPanel();

		if (withTabs) {
			tabPanel = new TabPanel();
			layout.add(tabPanel);
		}
		else {
			addMetadata();	
			contentTable = new FlexTable();
			layout.add(contentTable);
		}
		setContent(layout);
	}
	
	/** form layout properties */
	private void properties(){
		
		displayHeaderIcon(true);
		propertiesButtonsPanel();
		setSize("100%","100%");
		layout.setWidth("100%");
		layout.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
		layout.setCellHeight(metaInfoPanel, "22px");
		
		if (withTabs) {
			layout.setCellVerticalAlignment(tabPanel, VerticalPanel.ALIGN_TOP);	
			layout.setCellWidth(tabPanel, "100%");
			layout.setStylePrimaryName("imogene-Form");	
			layout.addStyleDependentName(color);
			tabPanel.setWidth("100%");
			tabPanel.setStylePrimaryName("imogene-TabBar");
			tabPanel.addStyleDependentName(color);
			tabPanel.getTabBar().setStylePrimaryName("imogene-TabBar");
			tabPanel.getTabBar().addStyleDependentName(color);
			layout.setCellWidth(tabPanel, "100%");
		}
		else {
			layout.setCellVerticalAlignment(contentTable, VerticalPanel.ALIGN_TOP);	
			layout.setCellWidth(contentTable, "100%");
			layout.setStylePrimaryName("imogene-Form");	
			layout.addStyleDependentName(color);
			contentTable.setWidth("100%");
			layout.setCellWidth(contentTable, "100%");
		}
		
		creationLabel.setStyleName("imogene-greytext");
		modificationLabel.setStyleName("imogene-greytext");
		idLabel.setStyleName("imogene-greytext");
	}
	
	/**
	 * buttons behavior
	 */
	private void behavior(){
		editButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
				setEditable(true);
				setTitle(computeModificationTitle());
			}
		});
		saveButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				setTitle(title);
				if (Window.confirm(BaseNLS.constants().confirmation_save()))
					save();
				
			}
		});
		cancelButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				LocalSession.get().removeFromEdited(hashCode());
				if(isNew){
					closeForm();
				}else{
					setTitle(title);
					setEditable(false);
					cancel();
				}
			}
		});		
		closeButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
				closeForm();
			}			
		});
	}
	
	/** buttons panel layout */
	private void layoutButtonsPanel(){
		
		/* form metadata */
		metaInfoPanel = new VerticalPanel();
		idLabel = new HTML();
		idLabel.setHTML("");
		metaInfoPanel.add(idLabel);
		modificationLabel = new HTML();
		modificationLabel.setHTML("");
		metaInfoPanel.add(modificationLabel);
		creationLabel = new HTML();
		creationLabel.setHTML("");
		metaInfoPanel.add(creationLabel);
		if(withTabs)
			metaInfoPanel.add(new HTML("<br/>"));
		
		/* form buttons */
		saveButton = new PushButton(BaseNLS.constants().button_save());
		editButton = new PushButton(BaseNLS.constants().button_edit());
		cancelButton = new PushButton(BaseNLS.constants().button_cancel());
		closeButton = new PushButton(BaseNLS.constants().button_close());
		printButton = new PushButton();
		addButton(saveButton);
		addButton(cancelButton);
		addButton(editButton);
		addButton(printButton);
		addButton(closeButton);
	}
	
	/** buttons panel layout properties */
	private void propertiesButtonsPanel(){
		
		metaInfoPanel.setSpacing(0);
		metaInfoPanel.setStylePrimaryName("imogene-FormMetadata");	
		
		saveButton.setStylePrimaryName("imogene-Button");
		editButton.setStylePrimaryName("imogene-Button");
		cancelButton.setStylePrimaryName("imogene-Button");
		closeButton.setStylePrimaryName("imogene-Button");
		printButton.setStylePrimaryName("imogene-Button");
		closeButton.setVisible(false);
	}
	
	/**
	 * Adds a section to the form
	 * @param newSection the section to be added to the form
	 */
	public void addSection(DisclosureContainerComposite newSection){
		if (withTabs) {
			addSection(newSection, "");
		}
		else {
			addSection(newSection, 1,1);
		}
	}
	
	/**
	 * Adds a section to the form
	 * @param newSection the section to be added to the form as a tabulation
	 * @param title the title to be displayed in tabulation title
	 */
	public void addSection(DisclosureContainerComposite newSection, String title){
		if (withTabs) {
			tabPanel.add(newSection, title);			 
			if (tabPanel.getWidgetCount()==1)
				tabPanel.selectTab(0);
		}
		else {
			addSection(newSection, 1,1);
		}
	}
	
	/**
	 * Adds a section to the form
	 * @param newSection the section to be added to the form as a table cell
	 * 
	 */
	public void addSection(DisclosureContainerComposite newSection, int rowSpan, int colSpan){
				
		if (withTabs) {
			addSection(newSection, "");
		}
		else {
			int countRow = contentTable.getRowCount();
			if(countRow==0){
				contentTable.setWidget(0, 0, newSection);
			}else{
				int countCell = contentTable.getCellCount(countRow-1);
				boolean lastColSpan = contentTable.getFlexCellFormatter().getColSpan(countRow-1, countCell-1)==2;
				if(countCell<MAX_COL && !lastColSpan){
					if(colSpan<2){
						contentTable.setWidget(countRow-1, countCell, newSection);
					}
					else{
						contentTable.setWidget(countRow-1, countCell, new Label(""));
						contentTable.setWidget(countRow, 0, newSection);
					}
				}
				else{								
					contentTable.setWidget(countRow, 0, newSection);
				}
			}		
			int lastRow = contentTable.getRowCount()-1;
			int lastCell = contentTable.getCellCount(lastRow)-1;
			newSection.setWidth("100%");
			contentTable.getCellFormatter().setVerticalAlignment(lastRow, lastCell, HasAlignment.ALIGN_TOP);			
			if(colSpan>1)
				contentTable.getFlexCellFormatter().setColSpan(lastRow,lastCell, colSpan);
			else
				contentTable.getCellFormatter().setWidth(lastRow, lastCell, "50%");
		}
	}
	
	/**
	 * Adds metadata information to the form
	 */
	public void addMetadata() {
		if (withTabs) {
			tabPanel.add(metaInfoPanel, BaseNLS.constants().form_metadata_title());
		}
		else {
			layout.add(metaInfoPanel);	
		}
	}
	
	/**
	 * Adds metadata information to the form
	 */
	public void addMetadata(GroupField section) {
		metaInfoPanel.add(section);
		addMetadata();
	}
	
	
	
	/**
	 * Sets the status of the form : editable or not
	 * @param editable true if the form is editable
	 */
	public void setEditable(boolean editable){
		editButton.setVisible(!editable && isModifiable);
		printButton.setVisible(!editable);
		saveButton.setVisible(editable);
		cancelButton.setVisible(editable);	
		closeButton.setVisible(!editable);
	}
	
	/**
	 * Displays or hides a button
	 * @param button the button to be displayed or hidden
	 * @param display true if the button has to be displayed
	 */
	public void displayButton(PushButton button, boolean display) {
		button.setVisible(display);
	}
	
	/**
	 * Computes the string to be displayed when the form 
	 * is in edit mode.
	 * @return the new string to display as title.
	 */
	private String computeModificationTitle(){
		return BaseNLS.messages().form_modification_title(title);
	}
	
	/**
	 * Closes the form if it is closeable
	 */
	protected void closeForm(){
		if(closeable){
			if(container!=null) {
				int last = container.countForms();
				if (last>0)
					container.removeForm(AbstractFormComposite.this);
				else
					returnToList();
					
			}
			else {
				AbstractFormComposite.this.removeFromParent();
			}
		}
	}
	
	@Override
	public void setTitle(String pTitle){
		title = pTitle;
		super.setTitle(title);
	}
	
	/**
	 * Sets the container in 
	 * which the embedded form will be opened.
	 * @param pContainer the form receiver
	 */
	public void setFormContainer(TaskWrapperPanel pContainer){
		container = pContainer;
	}	
	
	/**
	 * Sets this form as closeable by a 'close' button.
	 * @param pCloseable true if this form is closeable.
	 */
	public void setCloseable(boolean pCloseable){
		closeable = pCloseable;
		closeButton.setVisible(closeable);
	}
	
	/**
	 * Sets if this form is modifiable.
	 * @param pModifiable true if the form is modifiable
	 */
	public void setModifiable(boolean pModifiable){
		isModifiable = pModifiable;
		editButton.setVisible(isModifiable);
	}
	
	public void setMetaData(ImogBean bean){
		String createUpdate = "";
		String modifUpdate = "";
		String idUpdate = "";
		if(bean.getCreationDate()!=null && bean.getCreator()!=null){
			String created = TextFormatUtil.getDate(bean.getCreationDate());
			String creator = bean.getCreator();
			createUpdate = BaseNLS.messages().form_metadata_creation(created,creator);
		}
		if(bean.getLastModificationDate()!=null && bean.getModifier()!=null){
			String modified = TextFormatUtil.getDate(bean.getLastModificationDate());
			String modifier = bean.getModifier();
			modifUpdate = BaseNLS.messages().form_metadata_modification(modified,modifier);
		}	
		if(bean.getId()!=null){
			idUpdate = BaseNLS.messages().form_metadata_id(bean.getId());
		}
		idLabel.setHTML(idUpdate);
		modificationLabel.setHTML(modifUpdate);
		creationLabel.setHTML(createUpdate);
	}

	/**
	 * Action when the save button is clicked
	 */
	protected abstract void save();
	
	/**
	 * Action when the cancel button is clicked
	 */
	protected abstract void cancel();
	
	
	/**
	 * Action when the close button is clicked and the current form is the only form remaining
	 * in the container -> return to the entity list
	 */
	protected abstract void returnToList();		
}
