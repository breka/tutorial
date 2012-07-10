package org.imogene.web.gwt.client.ui.field.paginatedList;

import org.imogene.common.entity.ImogBean;
import org.imogene.web.gwt.client.ui.field.MainFieldsUtil;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Popup panel that contains the list of bean instances
 * 
 * @author MEDES-IMPS
 */
public class ImogPaginatedListBoxPopup<T extends ImogBean> extends PopupPanel
		implements ClickHandler {

	private ImogPaginatedListBox<T> parentBox;

	/*
	 * case when the bean instance list is paginated through asynchronous call
	 * to the database
	 */
	private ImogPaginatedListOneSelect paginatedList = null;
	/* case when the bean instances have to be explicitly added to the list */
	private ImogFixedList fixedList = null;

	boolean isPaginated = true;

	/**
	 * Create a new ImogPaginatedListBoxPopup that will use the specified data
	 * provider to retrieve bean instances by asynchronous calls to the database
	 * 
	 * @param provider
	 *            The data provider
	 * @param mainFieldsUtil
	 *            the utility class to get a display value of the list bean
	 *            instances
	 * @param parentBox
	 *            The parent composite
	 */
	public ImogPaginatedListBoxPopup(ImogPaginatedListBoxDataProvider provider,
			MainFieldsUtil mainFieldsUtil, ImogPaginatedListBox<T> parentBox) {

		super(true);
		this.parentBox = parentBox;
		this.setStyleName("ImogListBox-PopupPanel ");

		paginatedList = new ImogPaginatedListOneSelect(provider, mainFieldsUtil);
		paginatedList.addClickListener(this);
		add(paginatedList);
	}

	/**
	 * Create a new ImogPaginatedListBoxPopup whose bean instance list will be
	 * explicitly defined using the addItem method.
	 * 
	 * @param parentBox
	 *            The parent composite
	 */
	public ImogPaginatedListBoxPopup(ImogPaginatedListBox<T> parentBox) {

		super(true);
		this.parentBox = parentBox;
		this.setStyleName("ImogListBox-PopupPanel ");

		fixedList = new ImogFixedList();
		fixedList.addClickListener(this);
		add(fixedList);
		isPaginated = false;
	}

	/**
	 * Explicitly adds a bean instance to the list (to be used when the list is
	 * not paginated and no data provider is defined) Used after the constructor
	 * ImogPaginatedListBoxPopup(ImogPaginatedListBox<T> parentBox)
	 * 
	 * @param display
	 *            the bean display value
	 * @param id
	 *            the bean id
	 * @param value
	 *            the bean instance
	 */
	public void addItem(String display, String id, ImogBean value) {
		if (fixedList != null) {
			fixedList.addItem(display, id, value);
		}
	}

	/**
	 * Informs if the list box is paginated
	 * 
	 * @return true if the list box is paginated
	 */
	public boolean isPaginated() {
		return isPaginated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event
	 * .dom.client.ClickEvent)
	 */
	public void onClick(ClickEvent event) {

		if (paginatedList != null) {

			if (event.getSource().equals(paginatedList.getListBox())) {
				ImogBean value = paginatedList.getValue();
				T result = (T) value;
				parentBox.setValue(result, true);
				hide();
			}
			if (event.getSource().equals(paginatedList.getClearImage())) {
				parentBox.setValue(null, true);
				hide();
			}
		} else if (fixedList != null) {

			if (event.getSource().equals(fixedList.getListBox())) {
				ImogBean value = fixedList.getValue();
				T result = (T) value;
				parentBox.setValue(result, true);
				hide();
			}
			if (event.getSource().equals(fixedList.getClearImage())) {
				parentBox.setValue(null, true);
				hide();
			}
		}
	}

	@Override
	public void show() {
		if (paginatedList != null)
			paginatedList.fillList();
		super.show();
	}

}
