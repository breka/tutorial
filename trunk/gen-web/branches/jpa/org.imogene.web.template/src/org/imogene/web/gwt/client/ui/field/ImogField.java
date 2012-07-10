package org.imogene.web.gwt.client.ui.field;

public interface ImogField<E> {

	public boolean validate();

	public void setLabel(String label);

	public String getLabel();

	public void setValue(E value);

	public void setValue(E value, boolean notifyChange);

	public E getValue();

	public void setEnabled(boolean editable);

	public boolean isEdited();
}
