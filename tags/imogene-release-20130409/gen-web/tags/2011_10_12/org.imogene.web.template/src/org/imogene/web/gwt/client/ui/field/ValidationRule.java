package org.imogene.web.gwt.client.ui.field;

/**
 * DEscription of a regex validation rule for the fields
 * @author Medes-IMPS
 */
public class ValidationRule {
	
	private String text;
	
	private String regex;
	
	public ValidationRule(){		
	}
	
	public ValidationRule(String pText, String pRegex){
		text = pText;
		regex = pRegex;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
}
