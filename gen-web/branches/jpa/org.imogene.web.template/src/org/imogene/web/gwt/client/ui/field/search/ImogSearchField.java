package org.imogene.web.gwt.client.ui.field.search;

public interface ImogSearchField<E> {

	/**
	 * Sets the field label
	 * 
	 * @param label
	 *            label of the field to be searched
	 */
	public void setLabel(String label);

	/**
	 * Gets the field label
	 */
	public String getLabel();

	/**
	 * Gets the value to be searched
	 */
	public E getValue();

	/**
	 * Gets the operator value for the search
	 */
	public String getOperatorValue();

	/**
	 * Clears composite operator and value
	 */
	public void cancel();

	/**
	 * Returns if the entered values are valid for search
	 * 
	 * @return true if the entered values are valid for search
	 */
	public boolean toBeSearched();

}
