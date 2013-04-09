package org.imogene.gwt.widgets.client.dynaTable;
import com.google.gwt.core.client.GWT;

public class DynaTableNLS{

	private static DynaTableConstants _const = (DynaTableConstants)GWT.create(DynaTableConstants.class);

	public static DynaTableConstants constants(){
		return _const;
	}
}
