package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Creates a Table composite with 2 columns. 
 * The table is not filled by rows, but by columns.
 * Each column is independent from the other one, 
 * the 2 columns are filled independently
 * @author MEDES-IMPS
 */
public class FieldTableComposite extends Composite {
	
	
	private Composite leftColumnComposite;
	private Composite rightColumnComposite;
	

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param toolkit
	 */
	public FieldTableComposite(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style);
		
		this.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));	

		/* 2 column table */
		GridLayout fieldTableCompositeLayout = new GridLayout(2, false);
		fieldTableCompositeLayout.makeColumnsEqualWidth = true;
		fieldTableCompositeLayout.marginWidth = 0;
		fieldTableCompositeLayout.marginHeight = 0;
		fieldTableCompositeLayout.horizontalSpacing = 0;
		fieldTableCompositeLayout.verticalSpacing = 0;	
		this.setLayout(fieldTableCompositeLayout);
		//GridData fieldTableCompositeLayoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		//this.setLayoutData(fieldTableCompositeLayoutData);
		
		/* left column of the table */
		
		leftColumnComposite = toolkit.createComposite(this);
		
		GridLayout leftColumnCompositeLayout = new GridLayout(1, false);
		leftColumnCompositeLayout.marginWidth = 0;
		leftColumnCompositeLayout.marginHeight = 0;
		leftColumnCompositeLayout.horizontalSpacing = 0;
		leftColumnCompositeLayout.verticalSpacing = 0;
		leftColumnComposite.setLayout(leftColumnCompositeLayout);	
		
		GridData leftColumnCompositeLayoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		leftColumnComposite.setLayoutData(leftColumnCompositeLayoutData);
		
		/* right column of the table */
		
		rightColumnComposite = toolkit.createComposite(this);
		
		GridLayout rightColumnCompositeLayout = new GridLayout(1, false);
		rightColumnCompositeLayout.marginWidth = 0;
		rightColumnCompositeLayout.marginHeight = 0;
		rightColumnCompositeLayout.horizontalSpacing = 0;
		rightColumnCompositeLayout.verticalSpacing = 0;	
		rightColumnComposite.setLayout(rightColumnCompositeLayout);
		
		GridData rightColumnCompositeLayoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		rightColumnComposite.setLayoutData(rightColumnCompositeLayoutData);
	}
	
	/**
	 * Gets the left column composite of the table. 
	 * This composite has a GridLayout with one column
	 * @return left column composite of the table
	 */
	public Composite getLeftColumnComposite() {
		return leftColumnComposite;
	}
	
	/**
	 * Gets the right column composite of the table. 
	 * This composite has a GridLayout with one column
	 * @return right column composite of the table
	 */
	public Composite getRightColumnComposite() {
		return rightColumnComposite;
	}
	

}
