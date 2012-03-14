package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.wrapper.CoreMessages;


/**
 * Widget that enables to create a form field
 * @author MEDES-IMPS
 */
public abstract class FormFieldComposite extends Composite {
	
	private static String endFieldLabelView = ":";
	private static String endFieldLabelEditRequired = ":*";
	
	private boolean isRequired;
	
	private String help = new String();
	
	private Image indent = ImogPlugin.getImageDescriptor("icons/indent.png").createImage();	
	private Label imageLabel;
	
	private Image helpImage = ImogPlugin.getImageDescriptor("icons/system_help_16.png").createImage();
	private Label helpButton;
	
	private Color labelColor;
	private Color errorColor;
	
	private Label fieldLabel;
	protected Control fieldValue;
	
	private boolean isInError = false;
	private Label errorLabel;
	
	private boolean isVisibilityDependent=false;
	
	private ToolTip tip;
	
	
	/**
	 * Creates a complete FormField composite with Label and Value fields
	 * @param parent field parent form
	 * @param toolkit
	 * @param fieldLabelText field label text
	 * @param isRequired true if the field can not be empty
	 */
	public FormFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent){
		super(parent, SWT.NONE);	
		this.isRequired = isRequired;
		isVisibilityDependent = dependent;
		createColors(toolkit);
		composeComposite(toolkit, fieldLabelText);	
		createToolTip(parent.getShell());
	}
	
	/**
	 * Creates an empty container that will be filled using method createComposite
	 * @param parent
	 */
	public FormFieldComposite(Composite parent) {
		super(parent, SWT.NONE);		
	}
	
	
	
	@Override
	public boolean setFocus() {
		return helpButton.setFocus();
	}

	/**
	 * Creates the composite. To be used after constructor FormFieldComposite(Composite parent)
	 * @param toolkit
	 * @param fieldLabelText
	 * @param isRequired
	 */
	protected void createComposite(FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this.isRequired = isRequired;
		createColors(toolkit);
		composeComposite(toolkit, fieldLabelText);
	}
	
	/** create the tooltip associated with this field */
	private void createToolTip(Shell shell){
		tip = new ToolTip(shell, SWT.BALLOON);
		tip.setAutoHide(true);		
		tip.setVisible(false);
	}
	
	/**
	 * 
	 * @param toolkit
	 */
	private void createColors(FormToolkit toolkit) {
		labelColor = toolkit.getColors().getColor(IFormColors.TITLE);
		errorColor = new Color(Display.getCurrent(),255, 102, 86);
	}
	
	/**
	 * 
	 * @param toolkit
	 * @param fieldLabelText field label text
	 */
	private void composeComposite(FormToolkit toolkit, String fieldLabelText) {
		
		// set layout
		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = 10;
		layout.horizontalSpacing = 5;
		this.setLayout(layout);
		this.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		/* indent image */
		imageLabel = toolkit.createLabel(this, "");
		imageLabel.setImage(indent);
		GridData imageLabelData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_CENTER);
		imageLabelData.exclude=!isVisibilityDependent;
		imageLabel.setLayoutData(imageLabelData);
		
		/* help button to display a tooltip */
		helpButton = toolkit.createLabel(this, "", SWT.NONE);
		helpButton.setImage(helpImage);
		GridData helpData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_CENTER);
		helpButton.setLayoutData(helpData);
		helpButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {								
			}

			@Override
			public void mouseDown(MouseEvent e) {								
			}

			@Override
			public void mouseUp(MouseEvent e) {				
				if(!help.equals(""))
					tip.setMessage(help);
				else
					tip.setMessage(fieldLabel.getText());
				tip.setLocation(e.widget.getDisplay().getCursorLocation());
				tip.setVisible(true);
			}
			
		});
		
		// add field label
		fieldLabel = toolkit.createLabel(this, fieldLabelText, SWT.WRAP);
		completeFieldLabel();
		fieldLabel.setForeground(labelColor);
		GridData labelLayoutData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_CENTER);
		labelLayoutData.widthHint = Integer.parseInt(CoreMessages.getString("form_field_label_width"));
		fieldLabel.setLayoutData(labelLayoutData);
		if(!isVisibilityDependent){
			FontData labelFontData = new FontData("Arial", 10, SWT.BOLD);
			fieldLabel.setFont(new Font(getDisplay(), labelFontData));
		}
		
		// add field value
		createFieldValue(toolkit);
		fieldValue.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER ));
		
		// add error message label
		errorLabel = toolkit.createLabel(this, "");
		errorLabel.setForeground(errorColor);
		GridData errorGridData = new GridData(GridData.VERTICAL_ALIGN_CENTER);
	    errorGridData.horizontalSpan = 2;
	    errorGridData.exclude= true;
	    errorLabel.setLayoutData(errorGridData);
	}
	
	/**
	 * True if the visibility of this field depends
	 * on the value of an other	
	 * @return true is the visibility is dependent
	 */
	public boolean isVisibilityDependent() {
		return isVisibilityDependent;
	}

	/**
	 * Set if the visibility of this field depends
	 * on the value of an other	
	 * @param dependent true is the visibility is dependent
	 */
	public void setVisibilityDependent(boolean dependent) {
		isVisibilityDependent = dependent;
	}
	
	/**
	 * Set the help message
	 * @param message the message text
	 */	
	public void setHelp(String message){
		help = message;
	}
	
	/**
	 * Set if this field is gone (= visibility is false, and not considered during layout)
	 * @param gone true if the field is gone
	 */
	public void setGone(boolean gone){
		setVisible(!gone);
		Object layoutData = getLayoutData();
		if(layoutData instanceof RowData){
			((RowData)layoutData).exclude=gone;
		}
		if(layoutData instanceof GridData){
			((GridData)layoutData).exclude=gone;
		}				
		getParent().layout(true, true);		
	}

	/**
	 * 
	 * @return
	 */
	public abstract Control getValueControl();

	/**
	 * 
	 * @param errorMessage
	 */
	public void setInError (String errorMessage) {
		// set error symbolization and message
		setLabelErrorColor(true);
		errorLabel.setText(errorMessage);	
		
		// make error message visible
		GridData errorGridData = (GridData) errorLabel.getLayoutData();
	    errorGridData.exclude= false;    	
	    this.getParent().layout();
	    
	    isInError = true;	
	}
	
	/**
	 * 
	 */
	public void setOk() {
		if (isInError) {	
			// set no error symbolization
			setLabelErrorColor(false);
			errorLabel.setText("");
			
			// make error message invisible
			GridData errorGridData = (GridData) errorLabel.getLayoutData();
			errorGridData.exclude= true;
			this.getParent().layout();

			isInError = false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean IsRequiredOk() {
		if (isEmpty()) {
			setInError(CoreMessages.getString("form_field_required"));
			return false;
		}
		else {
			setOk();
			return true;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean validateExpression(String expression, String displayFormat) {
		if (!isExpressionValidated(expression)) {
			setInError(CoreMessages.getString("form_field_wrong_format") + " " + displayFormat);
			return false;
		}
		else {
			setOk();
			return true;
		}
	}
	
	/**
	 * 
	 * @param error
	 */
	private void setLabelErrorColor(boolean error){
		if(error){
			fieldLabel.setForeground(errorColor);
		}else{
			fieldLabel.setForeground(labelColor);
		}
	}
	
	
	/**
	 * 
	 * @param editable true if the field can be edited
	 */
	public void setEditableField(boolean editable) {
		updateFieldLabel(editable);
		setEditable(editable);
	}	
	
	/**
	 * 
	 */
	private void completeFieldLabel() {
		String labelText = fieldLabel.getText();	
		if (isRequired)
			labelText = labelText + endFieldLabelEditRequired;												
		else
			labelText =  labelText + endFieldLabelView;
		fieldLabel.setText(labelText);
	}	
	
	/**
	 * 
	 * @param editable true if the field can be edited
	 */
	private void updateFieldLabel(boolean editable) {
		String labelText = fieldLabel.getText();		
		if (editable && isRequired) {
			labelText = labelText.replace(endFieldLabelView, endFieldLabelEditRequired);
		}				
		else {
			labelText =  labelText.replace(endFieldLabelEditRequired, endFieldLabelView);		
		}
		fieldLabel.setText(labelText);
	}
	
	/**
	 * 
	 * @param width
	 */
	protected void setValueFieldWidth(Integer width) {
		if(fieldValue.getLayoutData()!=null){
			GridData valueGridData = (GridData) fieldValue.getLayoutData();
			valueGridData.widthHint = width;
			valueGridData.grabExcessHorizontalSpace = false;
		}
	}
	
	/**
	 * 
	 * @param toolkit
	 */
	protected abstract void createFieldValue(FormToolkit toolkit) ;	
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isEmpty();	
	
	
	/**
	 * 
	 * @param expression
	 * @return
	 */
	protected abstract boolean isExpressionValidated(String regex);
	

	/**
	 * 
	 * @param editable true if the field can be edited
	 */
	protected abstract void setEditable(boolean editable) ;	
	
	/**
	 * 
	 * @param readonly true if the field should be readonly
	 */
	public abstract void setReadonly(boolean readonly) ;	
	
	
	@Override
	public void dispose() {
		errorColor.dispose();	
		labelColor.dispose();
		super.dispose();
	}
	
	/**
	 * Get the image used to indent this field.
	 * @return the indent image
	 */
	protected Image getIndentImage(){
		return indent;
	}
		
}
