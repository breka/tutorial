package org.imogene.web.gwt.client.ui.field;

import java.util.HashMap;
import java.util.Map;

import org.imogene.common.entity.ImogBean;
import org.imogene.web.gwt.client.i18n.BaseNLS;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class ImogRelationSelectionBox<T extends ImogBean> extends
		PopupPanel implements ClickHandler {

	/* status */
	protected Map<String, T> alls = new HashMap<String, T>();

	/* widgets */
	private VerticalPanel panel;
	protected ListBox listSyncs;
	private ImogRelationList<T> parentField;
	private HTML title;
	private String color;

	/* buttons */
	private HorizontalPanel buttons;
	private PushButton ok;
	private PushButton cancel;

	public ImogRelationSelectionBox(String colorStyle) {
		super(true);
		color = colorStyle;
		layout();
		properties();
		setBehavior();
	}

	private void layout() {
		panel = new VerticalPanel();

		title = new HTML();
		panel.add(title);

		listSyncs = new ListBox(true);
		panel.add(listSyncs);

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

		listSyncs.setVisibleItemCount(6);
		listSyncs.setStylePrimaryName("imogene-RelationSelectionBox-Listbox");

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
	 * Set the parent field consumer of this box.
	 * 
	 * @param parent
	 *            the parent field
	 */
	public void setParentField(ImogRelationList<T> parent) {
		parentField = parent;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(ok)) {
			for (int i = 0; i < listSyncs.getItemCount(); i++) {
				if (listSyncs.isItemSelected(i)) {
					parentField.addValue(listSyncs.getItemText(i),
							alls.get(listSyncs.getValue(i)));
				}
			}
			hide();
		}
		if (event.getSource().equals(cancel)) {
			hide();
		}
	}

	/**
	 * Show the popup panel panel at the given position
	 * 
	 * @param left
	 *            the absolute left
	 * @param top
	 *            the absolute top
	 */
	public void show(int left, int top) {
		listSyncs.clear();
		populate();
		setPopupPosition(left, top);
		show();
	}

	protected boolean isPresent(T toTest) {
		return parentField.isPresent(toTest);
	}

	public abstract void populate();
}
