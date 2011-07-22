package org.imogene.web.gwt.client.ui.field.search;

import org.imogene.web.gwt.client.ui.form.DisclosureContainerComposite;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


/**
 * Define a group widget for search fields
 * @author Medes-IMPS
 */
public class SearchGroupField extends DisclosureContainerComposite {
	
	public FlexTable table;
	
	public SearchGroupField(){
		super();
		layout();
	}
	
	public SearchGroupField(String title){
		super(title);
		layout();
	}
	
	public SearchGroupField(String title, Image image){
		this(title);
		setImage(image);
	}
	
	private void layout(){
		table = new FlexTable();
		setContent(table);		
		properties();
	}
	
	private void properties(){
		table.getColumnFormatter().addStyleName(0, "imogene-Form-labelColumn");
		//table.getColumnFormatter().setWidth(1, "120");
	}
	
	/**
	 * Add a field to this group field
	 * @param field the field to add
	 */
	public void addField(ImogSearchFieldAbstract<?> field){
		int rowCount = table.getRowCount();
		Label label = new Label(field.getLabel());
		label.addStyleName("imogene-Form-labelColumn");
		label.setWordWrap(true);
		table.setWidget(rowCount, 0, label);
		table.setWidget(rowCount, 1, field);
	}
	
	/**
	 * Add a standard widget that will be 2 column span.
	 * @param w the widget to add.
	 */
	public void addWidget(Widget w){
		int rowCount = table.getRowCount();
		table.setWidget(rowCount, 0, w);
		table.getFlexCellFormatter().setColSpan(rowCount, 0, 2);
	}

	
}
