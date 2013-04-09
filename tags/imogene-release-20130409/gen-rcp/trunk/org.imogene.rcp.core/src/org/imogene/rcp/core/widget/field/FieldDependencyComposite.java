package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.wrapper.CoreMessages;


public class FieldDependencyComposite extends Composite {
	
	private Image indentImage;
	
	public FieldDependencyComposite(Composite parent, int style) {
		super(parent,  style);
		
		indentImage = ImogPlugin.getImageDescriptor("icons/indent.png").createImage();
		
		// set layout
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;	
		
		this.setLayout(layout);
		
		this.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));	
	}
	
	/**
	 * 
	 * @param parentField
	 */
	public void addParentField(FormFieldComposite parentField, boolean fill_horizontal) {

		GridData layoutData ;
		if (fill_horizontal)
			layoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		else {
			layoutData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			layoutData.widthHint = Integer.parseInt(CoreMessages.getString("form_one_column_width"));
		}
		layoutData.horizontalSpan = 2;
		parentField.setLayoutData(layoutData);		
	}
	
	/**
	 * 
	 * @return
	 */
	public Label addChildIndent() {
		
		/* ident image */
		Label indentLabel = new Label(this, SWT.NONE);
		indentLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		indentLabel.setImage(indentImage);
		GridData labelData = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		labelData.horizontalIndent = Integer.parseInt(CoreMessages.getString("form_field_dependency_indent"));
		indentLabel.setLayoutData(labelData);
		return indentLabel;
	}
	
	
	/**
	 * 
	 * @param childField
	 */
	public void addChildField(FormFieldComposite childField, Label indentLabel, boolean fill_horizontal) {
		
		GridData layoutData ;
		if (fill_horizontal)
			layoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		else {
			layoutData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			layoutData.widthHint = Integer.parseInt(CoreMessages.getString("form_one_column_width"));
		}
		
		childField.setLayoutData(layoutData);
		childField.setData(indentLabel);
	}
	
	
	/**
	 * 
	 * @param childField
	 * @param isVisible
	 */
	public void setChildVisible(FormFieldComposite childField, boolean isVisible) {
		childField.setVisible(isVisible);
		((GridData)childField.getLayoutData()).exclude = !isVisible;

		Label indentLabel = (Label)childField.getData();
		indentLabel.setVisible(isVisible);
		((GridData)indentLabel.getLayoutData()).exclude = !isVisible;
	}
	
	
	@Override
	public void dispose() {
		indentImage.dispose();	
		super.dispose();
	}
	
	

}
