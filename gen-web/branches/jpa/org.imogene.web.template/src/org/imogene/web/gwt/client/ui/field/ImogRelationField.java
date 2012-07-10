package org.imogene.web.gwt.client.ui.field;

import org.imogene.common.entity.ImogBean;
import org.imogene.web.gwt.client.i18n.BaseNLS;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Field that enables to handle a relation one-to-one or one-to*
 * 
 * @author Medes-IMPS
 * 
 * @param <T>
 */
public class ImogRelationField<T extends ImogBean> extends ImogFieldAbstract<T> {

	/** status */
	private T bean;
	private String thisLabel;
	private boolean edited = false;
	private boolean canCreateEntity = true;

	/** widgets */
	private HorizontalPanel layout;
	private Image errorImage;
	private Image addImage;
	private Image viewImage;
	private TextBox textBox;

	public ImogRelationField() {
		layout();
		properties();
	}

	public ImogRelationField(String label) {
		this();
		thisLabel = label;
	}

	public ImogRelationField(String label, boolean canCreateEntity) {
		this();
		thisLabel = label;
		this.canCreateEntity = canCreateEntity;
	}

	private void layout() {
		layout = new HorizontalPanel();
		errorImage = new Image(GWT.getModuleBaseURL());
		addImage = new Image(GWT.getModuleBaseURL()
				+ "/images/relation_add.png");
		addImage.setTitle(BaseNLS.constants().button_add());
		viewImage = new Image(GWT.getModuleBaseURL()
				+ "/images/relation_view.png");
		viewImage.setTitle(BaseNLS.constants().button_view());
		textBox = new TextBox();
		layout.add(errorImage);
		layout.add(textBox);
		layout.add(viewImage);
		layout.add(addImage);
		initWidget(layout);
	}

	private void properties() {
		// layout.setCellWidth(errorImage, "16px");
		viewImage.setStyleName("imogene-FormImageLink");
		viewImage.setVisible(false);
		addImage.setStyleName("imogene-FormImageLink");
		// layout.setCellWidth(viewImage, "16px");
		layout.setCellVerticalAlignment(viewImage, HorizontalPanel.ALIGN_MIDDLE);
		// layout.setCellWidth(addImage, "16px");
		layout.setCellVerticalAlignment(addImage, HorizontalPanel.ALIGN_MIDDLE);
		errorImage.setVisible(false);
		textBox.setStylePrimaryName("imogene-FormText");
		textBox.setEnabled(false);
	}

	public TextBox getEmbedded() {
		return textBox;
	}

	public void setInError(boolean inError) {
		if (inError)
			textBox.addStyleDependentName("error");
		else
			textBox.removeStyleDependentName("error");
	}

	public void setEnabled(boolean enabled) {
		// textBox.setEnabled(enabled && !isLocked());
		if (!enabled || isLocked()) {
			textBox.addStyleDependentName("disabled");
			addImage.setVisible(false);
		} else {
			textBox.removeStyleDependentName("disabled");
			if (canCreateEntity)
				addImage.setVisible(true);
			else
				addImage.setVisible(false);
		}
		edited = enabled;
	}

	/*
	 * public void addItem(String display, String id, T value){
	 * values.add(value); textBox.addItem(display, id); }
	 */

	/*
	 * public int getIndexForValue(String value){ for(int i = 0;
	 * i<textBox.getItemCount(); i++){ if(textBox.getValue(i).equals(value))
	 * return i; } return -1; }
	 */

	public void setAddClickHandler(ClickHandler handler) {
		addImage.addClickHandler(handler);
	}

	public void setViewClickHandler(ClickHandler handler) {
		viewImage.addClickHandler(handler);
	}

	@Override
	public boolean validate() {
		if (isLocked())
			return true;
		if (isMandatory() && bean == null) {
			displayError(BaseNLS.constants().field_mandatory());
			return false;
		}
		hideError();
		return true;
	}

	@Override
	public String getLabel() {
		if (thisLabel != null)
			return thisLabel;
		return "";
	}

	@Override
	public T getValue() {
		return bean;
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
	}

	@Override
	public void setValue(T value) {
		bean = value;
		textBox.setText("");
		if (bean != null)
			viewImage.setVisible(true);
		else
			viewImage.setVisible(false);
	}

	@Override
	public void setValue(T value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}

	public void setValue(T value, String label) {
		textBox.setText(label);
		bean = value;
		if (bean != null)
			viewImage.setVisible(true);
		else
			viewImage.setVisible(false);
	}

	@Override
	public boolean isEdited() {
		return edited;
	}

}
