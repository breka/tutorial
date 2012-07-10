package org.imogene.web.gwt.client.util;

/**
 * Class that proposes tools to help in the management of the flash plugin. The
 * js library '' have to be present.
 * 
 * @author Medes-IMPS
 */
public class FlashTools {

	/**
	 * Return true is the flash plugin is installed and has the correct version.
	 * 
	 * @param majorVersion
	 *            the flash playre minimum major version
	 * @param minorVersion
	 *            the flash player minimum minor version
	 * @param requiredVersion
	 *            the flash player required version
	 * @return true if the needed flash plugin is installed
	 */
	public static final native boolean isFlashInstalled(int majorVersion,
			int minorVersion, int requiredVersion)/*-{
													var detected = $wnd.DetectFlashVer(majorVersion, minorVersion, requiredVersion);
													return detected;
													}-*/;

	/**
	 * Detect the installation of the flash player with a version > 8.0 return
	 * true if Flash Player 8.O or later installed
	 */
	public static boolean isFalshInstalled() {
		return isFlashInstalled(8, 0, 0);
	}

}
