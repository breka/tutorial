package org.imogene.rcp.core.wrapper;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CoreMessages {

	private static final String BUNDLE_NAME = "org.imogene.rcp.core.wrapper.core_messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private CoreMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
