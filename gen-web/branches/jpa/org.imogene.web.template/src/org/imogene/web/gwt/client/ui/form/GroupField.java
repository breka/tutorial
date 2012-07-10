package org.imogene.web.gwt.client.ui.form;

import org.imogene.web.gwt.client.ui.field.ImogField;
import org.imogene.web.gwt.client.ui.field.ImogFieldAbstract;
import org.imogene.web.gwt.client.ui.field.VisibilityChangeHandler;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Define a group field widget
 * 
 * @author Medes-IMPS
 */
public class GroupField extends DisclosureContainerComposite implements
		VisibilityChangeHandler {

	public FlexTable table;

	public GroupField() {
		super();
		layout();
	}

	public GroupField(String title) {
		super(title);
		layout();
	}

	public GroupField(String title, Image image) {
		this(title);
		setImage(image);
	}

	private void layout() {
		table = new FlexTable();
		setContent(table);
		properties();
	}

	private void properties() {
		table.getColumnFormatter().addStyleName(0, "imogene-Form-labelColumn");
	}

	/**
	 * Add a field to this group field
	 * 
	 * @param field
	 *            the field to add
	 */
	public void addField(ImogFieldAbstract<?> field) {
		int rowCount = table.getRowCount();
		Label label = new Label(field.getLabel());
		label.addStyleName("imogene-Form-labelColumn");
		label.setWordWrap(true);
		table.setWidget(rowCount, 0, label);
		table.setWidget(rowCount, 1, field);
		field.addVisibilityChangeHandler(this);
		if (field.isVisisbleDependent()) {
			label.addStyleName("imogene-visibleDependentLabel");
			field.addStyleName("imogene-visibleDependentField");
		}
		if (!field.isVisible())
			onVisibilityChange(false, field);
	}

	/**
	 * Add a standard widget that will be 2 column span.
	 * 
	 * @param w
	 *            the widget to add.
	 */
	public void addWidget(Widget w) {
		int rowCount = table.getRowCount();
		table.setWidget(rowCount, 0, w);
		table.getFlexCellFormatter().setColSpan(rowCount, 0, 2);
	}

	@Override
	public void onVisibilityChange(boolean visibility, ImogField<?> field) {
		for (int i = 0; i < table.getRowCount(); i++) {
			if (table.getCellCount(i) > 1
					&& field.equals(table.getWidget(i, 1))) {
				table.getRowFormatter().setVisible(i, visibility);
			}
		}
	}

}
