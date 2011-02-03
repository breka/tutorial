package org.imogene.rcp.core.widget;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class IntegerInputValidator implements VerifyListener {
	
	public void verifyText(VerifyEvent e) {		
		String string = e.text;
		char [] chars = new char [string.length ()];
		string.getChars (0, chars.length, chars, 0);
		for (int i=0; i<chars.length; i++) {
			if (!('0' <= chars [i] && chars [i] <= '9')) {
				e.doit = false;
				return;
			}
		}
	}
}
