package org.imogene.rcp.core.widget;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.tools.DateHelper;
import org.imogene.rcp.core.tools.DateSelector;
import org.imogene.rcp.core.wrapper.CoreMessages;


public class DateText extends Composite implements SelectionListener{
	
	private Text text;
	
	private Button button;
	
	private Image image;
	
	private FormToolkit toolkit;
	
	private Date date;
	
	private int widgetType = SWT.CALENDAR;
	
	/**
	 * Create a date text for the standard style
	 * @param parent the parent composite
	 */
	public DateText(Composite parent){
		this(parent, false);
	}
	
	/**
	 * Create a date text
	 * @param parent the parent composite
	 * @param formStyle true to set the HTMLForm style
	 */
	public DateText(Composite parent, boolean formStyle){
		super(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		setLayout(layout);
		
		
		if(formStyle){
			toolkit = new FormToolkit(parent.getDisplay());
			createFormWidget();
		}else{
			createStandardWidget();
		}
		text.setEditable(false);
		text.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.FILL_HORIZONTAL));
		
		image = ImogPlugin.getImageDescriptor("icons/calendar.gif").createImage();
		button.setImage(image);
		if (widgetType==SWT.TIME)
			button.setToolTipText(CoreMessages.getString("tooltip_time_open"));
		else
			button.setToolTipText(CoreMessages.getString("tooltip_date_open"));
		
		button.addSelectionListener(this);	
		button.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
	}
	
	/**
	 * Create a date text
	 * @param parent the parent composite
	 * @param formStyle true to set the HTMLForm style
	 * @param widgetType type of wanted widget (Calendar = SWT.CALENDAR, Time = SWT.TIME)
	 */
	public DateText(Composite parent, boolean formStyle, int widgetType){
		this(parent, formStyle);
		this.widgetType = widgetType;
	}

	/**
	 * create widget form the standard style
	 */
	private void createStandardWidget(){
		text = new Text(this, SWT.BORDER);		
		button = new Button(this, SWT.PUSH);		
	}
	
	/**
	 * create widgets for the HtmlForm style
	 */
	private void createFormWidget(){
		super.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		text = toolkit.createText(this, "");
		button =  toolkit.createButton(this, "", SWT.PUSH | SWT.FLAT);
		toolkit.paintBordersFor(this);
	}
	
	/**
	 * Set if this widget is editable or not
	 * @param editable true if the widget is editable.
	 */
	public void setEditable(boolean editable) {
			if(editable)
				button.setVisible(true);
			else{
				button.setVisible(false);
			}
	}
	
	/**
	 * Set if this widget is readonly or not. If yes, sets
	 * the widget as not editable and applies a light grey background
	 * @param readonly true if the widget is readonly
	 */
	public void setReadonly(boolean readonly) {
		setEditable(!readonly);
		if (readonly) 
			text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		else
			text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {				
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {		
		DateSelector ds = new DateSelector(Display.getCurrent().getActiveShell(), widgetType);
		if(date != null) ds.setDate(date);
		Object result = ds.open();
		if(result != null){
			date = (Date)result;
			
			if (widgetType==SWT.TIME)
				text.setText(DateHelper.toStringTime((Date)result));
			else
				text.setText(DateHelper.toString((Date)result));
			
			text.pack();
			pack();
		}
	}	
	
	/**
	 * Set the date displayed by this widget
	 * @param date the date to display
	 */
	public void setDate(Date date){
		this.date = date;
		
		if (widgetType==SWT.TIME)
			text.setText(DateHelper.toStringTime(date));
		else
			text.setText(DateHelper.toString(date));
		
	}
	
	/**
	 * Get the date displayed by this widget
	 * @return the date set.
	 */
	public Date getDate(){
		return date;
	}

	@Override
	public void dispose() {
		image.dispose();
		super.dispose();
	}
	
	@Override
	 public boolean isFocusControl() {  
	  return (text.isFocusControl() || button.isFocusControl() || super.isFocusControl());
	 }

	 /**
	  * Add a modify listener
	  * @param listener listener to add
	  */
	 public void addModifyListener(ModifyListener listener){
	  text.addModifyListener(listener);
	 }

	 /**
	  * Remove a modify listener
	  * @param listener the listener to remove
	  */
	 public void removeModifyListener(ModifyListener listener){
	  text.removeModifyListener(listener);
	 }

}
