package org.imogene.web.gwt.client.ui.field.paginatedList;

import java.util.HashSet;
import java.util.Set;

import org.imogene.common.criteria.ImogJunction;
import org.imogene.common.entity.ImogBean;
import org.imogene.web.gwt.client.ui.field.MainFieldsUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

/**
 * This class enables to display a listbox that manages a large quantity of data
 * through paginated lists. Data are retrieved by an asynchronous call, and
 * paginated in the list box.
 * 
 * @author MEDES-IMPS
 */
public class ImogPaginatedListBox<T extends ImogBean> extends Composite
		implements ClickHandler {

	/* listbox selected value */
	private T value;

	/* textBox to display the listbox selected value */
	private TextBox textBox;

	/* icon to open the listbox */
	private Image image;

	/* listbox and data provider */
	private ImogPaginatedListBoxPopup<T> popupList = null;
	private AbstractImogListBoxDataProvider provider;
	private MainFieldsUtil mainFieldsUtil;

	/* status */
	private boolean isEnabled = true;
	private boolean isPaginated = true;

	/* listeners */
	protected Set<ListValueChangeHandler> listValueChangeHandler = new HashSet<ListValueChangeHandler>();

	/**
	 * Simple constructor
	 */
	public ImogPaginatedListBox() {
		layout();
	}

	/**
	 * 
	 * @param isPaginated
	 */
	public ImogPaginatedListBox(boolean isPaginated) {
		layout();
		if (!isPaginated) {
			this.isPaginated = isPaginated;
			popupList = new ImogPaginatedListBoxPopup<T>(this);
		}
	}

	/**
	 * 
	 */
	private void layout() {

		HorizontalPanel panel = new HorizontalPanel();
		panel.setStyleName("ImogListBox");

		/* the left text box to display the listbox selected value */
		textBox = new TextBox();
		textBox.setReadOnly(true);
		textBox.setStyleName("ImogListBox-TexBox");
		textBox.addClickHandler(this);
		panel.add(textBox);
		panel.setCellHorizontalAlignment(textBox,
				HasHorizontalAlignment.ALIGN_LEFT);
		panel.setCellVerticalAlignment(textBox,
				HasVerticalAlignment.ALIGN_MIDDLE);

		/* the right image to open the listbox, usually an arrow image */
		image = new Image(GWT.getModuleBaseURL() + "images/ico_downward.png");
		image.addClickHandler(this);
		panel.add(image);
		panel.setCellHorizontalAlignment(image,
				HasHorizontalAlignment.ALIGN_LEFT);
		panel.setCellVerticalAlignment(image, HasVerticalAlignment.ALIGN_TOP);
		panel.setCellWidth(image, "12px");

		initWidget(panel);
	}

	/**
	 * Get the value selected
	 * 
	 * @return the value selected
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Get the display text of the selected value
	 * 
	 * @return the display text of the selected value
	 */
	public String getText() {
		return textBox.getText();
	}

	/**
	 * Sets the listbox selected value
	 * 
	 * @param value
	 *            the selected value
	 */
	public void setValue(T value) {
		this.value = value;
		if (value != null)
			textBox.setText(mainFieldsUtil.getDisplayValue(value));
		else
			textBox.setText("");
	}

	/**
	 * Sets the listbox selected value
	 * 
	 * @param value
	 *            the selected value
	 * @param notifyChange
	 *            if true, notifies the value setup to the added handlers
	 */
	public void setValue(T value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyListValueChange();
	}

	/**
	 * set the text to display in the textBox.
	 * 
	 * @param text
	 */
	protected void setText(String text) {
		textBox.setText(text);
	}

	/**
	 * Sets if this widget is enabled for edition
	 * 
	 * @param enable
	 *            true if this widget is enable
	 */
	public void setEnabled(boolean enable) {
		isEnabled = enable;
		textBox.setReadOnly(!enable);
		image.setVisible(enable);
	}

	public void setTextBoxStyleName(String stylename) {
		textBox.setStyleName(stylename);
	}

	public void addTextBoxStyleName(String stylename) {
		textBox.addStyleName(stylename);
	}

	/**
	 * Sets the data provider that will feed the paginated lists
	 * 
	 * @param provider
	 *            the data provider
	 * @param mainFieldsUtil
	 *            the utility class to get a display value of the list bean
	 *            instances
	 */
	public void setDataProvider(AbstractImogListBoxDataProvider provider,
			MainFieldsUtil mainFieldsUtil) {
		this.provider = provider;
		this.mainFieldsUtil = mainFieldsUtil;
	}

	/**
	 * Explicitly adds a bean instance to the listbox. The list box will not be
	 * paginated
	 * 
	 * @param display
	 * @param id
	 * @param value
	 */
	public void addItem(String display, String id, T value) {
		this.isPaginated = false;
		if (popupList == null || popupList.isPaginated)
			popupList = new ImogPaginatedListBoxPopup<T>(this);
		popupList.addItem(display, id, value);
	}

	/**
	 * CLear the content of the MedanyListBox TextBox
	 */
	public void clear() {
		textBox.setText(null);
		value = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user
	 * .client.ui.Widget)
	 */
	public void onClick(ClickEvent event) {
		if (isEnabled
				&& (event.getSource().equals(image) || event.getSource()
						.equals(textBox))) {
			if (isPaginated) {
				popupList = new ImogPaginatedListBoxPopup<T>(provider,
						mainFieldsUtil, this);
			} else {
				if (popupList == null || popupList.isPaginated)
					popupList = new ImogPaginatedListBoxPopup<T>(this);
			}
			popupList.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop()
					+ getOffsetHeight() - 1);
			popupList.show();
		}
	}

	/**
	 * Notify handler the list selected value changed
	 */
	public void notifyListValueChange() {
		for (ListValueChangeHandler handler : listValueChangeHandler) {
			if (handler != null)
				handler.onListValueChange();
		}
	}

	/**
	 * Adds an handler notified when the list value changes
	 * 
	 * @param handler
	 *            the handler to add
	 */
	public void addListValueChangeHandler(ListValueChangeHandler handler) {
		listValueChangeHandler.add(handler);
	}

	/**
	 * Removes an handler notified when the list value change
	 * 
	 * @param handler
	 *            the handler to remove
	 */
	public void removeListValueChangeHandler(ListValueChangeHandler handler) {
		listValueChangeHandler.remove(handler);
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

	@Override
	protected void onUnload() {

		super.onUnload();
	}

}
