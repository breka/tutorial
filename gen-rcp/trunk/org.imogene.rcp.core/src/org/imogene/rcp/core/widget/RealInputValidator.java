package org.imogene.rcp.core.widget;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * Validator for real number input text field.
 * @author Medes-IMPS 
 */
public class RealInputValidator implements VerifyListener {
	
	public void verifyText(VerifyEvent e) {		
		String string = e.text;
		//System.out.println("Text to test : "+e.text);
		char [] chars = new char [string.length ()];
		string.getChars (0, chars.length, chars, 0);
		for (int i=0; i<chars.length; i++) {
			if (!(('0' <= chars [i] && chars [i] <= '9') || chars[i]=='.')) {
				e.doit = false;
				return;
			}
		}
		if(e.widget instanceof Text){
			Text textWidget = (Text)e.widget;
			String toTest = textWidget.getText()+string;
			//System.out.println("To test = " + toTest);
			if(!toTest.matches("([0-9]+(\\.)?)|([0-9]*\\.[0-9]+)")){
				e.doit = false;
				return;
			}
		}
	}

}
