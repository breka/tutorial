package org.imogene.web.gwt.client.ui.field.paginatedList;

import java.util.Set;

import org.imogene.common.criteria.ImogJunction;
import org.imogene.common.entity.ImogBean;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.ui.field.ImogPaginatedRelationList;
import org.imogene.web.gwt.client.ui.field.MainFieldsUtil;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImogRelationPaginatedSelectionBox<T extends ImogBean> extends
		PopupPanel implements ClickHandler {

	/* widgets */
	private VerticalPanel panel;
	private ImogPaginatedRelationList<T> parentField;
	private HTML title;
	private String color;

	/* buttons */
	private HorizontalPanel buttons;
	private PushButton ok;
	private PushButton cancel;

	private AbstractImogListBoxDataProvider provider;
	private MainFieldsUtil mainFieldsUtil;
	private ImogPaginatedListMultipleSelect pageList;

	// private HashSet<AssociationSelectionListener> listeners = new
	// HashSet<AssociationSelectionListener>();

	public ImogRelationPaginatedSelectionBox(String colorStyle,
			AbstractImogListBoxDataProvider provider,
			MainFieldsUtil mainFieldsUtil) {
		super(true);
		this.provider = provider;
		this.mainFieldsUtil = mainFieldsUtil;
		color = colorStyle;
		layout();
		properties();
		setBehavior();
	}

	private void layout() {
		panel = new VerticalPanel();

		title = new HTML();
		panel.add(title);

		pageList = new ImogPaginatedListMultipleSelect(provider, mainFieldsUtil);
		panel.add(pageList);

		layoutButtons();
		panel.add(buttons);
		setWidget(panel);
	}

	public void properties() {
		setStylePrimaryName("imogene-RelationSelectionBox");
		addStyleDependentName(color);

		panel.setSize("100%", "100%");

		title.setStylePrimaryName("imogene-RelationSelectionBox-title");
		title.addStyleDependentName(color);

		pageList.setStyleName("imogene-RelationSelectionBox-Listbox");

		buttons.setStylePrimaryName("imogene-RelationSelectionBox-ButtonsPanel");
		ok.setStylePrimaryName("imogene-Button");
		cancel.setStylePrimaryName("imogene-Button");
	}

	private void layoutButtons() {
		buttons = new HorizontalPanel();
		ok = new PushButton(BaseNLS.constants().button_ok());
		cancel = new PushButton(BaseNLS.constants().button_cancel());
		buttons.add(ok);
		buttons.add(cancel);
	}

	private void setBehavior() {
		ok.addClickHandler(this);
		cancel.addClickHandler(this);
	}

	public void setTitle(String text) {
		title.setText(text);
	}

	/**
	 * Sets the parent field consumer of this box.
	 * 
	 * @param parent
	 *            the parent field
	 */
	public void setParentField(ImogPaginatedRelationList<T> parent) {
		parentField = parent;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(ok)) {
			if (pageList.getValues() != null) {
				Set<ImogBean> values = pageList.getValues();
				for (ImogBean value : values) {
					T result = (T) value;
					if (!parentField.isPresent(result))
						parentField.addValue(
								mainFieldsUtil.getDisplayValue(result), result);
				}
			}
			hide();
		}
		if (event.getSource().equals(cancel)) {
			hide();
		}
	}

	/**
	 * Shows the popup panel panel at the given position
	 * 
	 * @param left
	 *            the absolute left
	 * @param top
	 *            the absolute top
	 */
	public void show(int left, int top) {
		pageList.fillList();
		setPopupPosition(left, top + 2);
		show();
	}

	protected boolean isPresent(T toTest) {
		return parentField.isPresent(toTest);
	}

	/**
	 * Sets filtering criterions for which values have to be filtered
	 * 
	 * @param criterions
	 *            ImogJunction including the criterions for which the values
	 *            have to be filtered
	 */
	public void setFilterParameters(ImogJunction criterions) {
		if (provider != null)
			provider.setFilterParameters(criterions);
	}
}
