package org.imogene.android.maps;


public final class MapsConstants {
	
	public static final int DEFAULT_LATIUDE_E6 = 43604472;
	public static final int DEFAULT_LONGITUDE_E6 = 1442975;
	public static final int DEFAULT_NORTH_E6 = 44119142;
	public static final int DEFAULT_WEST_E6 = 593262;
	public static final int DEFAULT_SOUTH_E6 = 42488302;
	public static final int DEFAULT_EAST_E6 = 2922363;
	
	public static final double DEFAULT_LATIUDE = DEFAULT_LATIUDE_E6 / 1E6;
	public static final double DEFAULT_LONGITUDE = DEFAULT_LONGITUDE_E6 / 1E6;
	public static final double DEFAULT_NORTH = DEFAULT_NORTH_E6 / 1E6;
	public static final double DEFAULT_WEST = DEFAULT_WEST_E6 / 1E6;
	public static final double DEFAULT_SOUTH = DEFAULT_SOUTH_E6 / 1E6;
	public static final double DEFAULT_EAST = DEFAULT_EAST_E6 / 1E6;
	
	/**
	 * latitude extra (must be a double value) to use when trying to visualize
	 * coordinates on a map, or trying to navigate to it
	 * This must be used with {@link MapsConstants.MIME_GPS}
	 */
	public static final String EXTRA_LATITUDE = "latitude";

	/**
	 * longitude extra (must be a double value) to use when trying to visualize
	 * coordinates on a map, or trying to navigate to it
	 * This must be used with {@link MapsConstants.MIME_GPS}
	 */
	public static final String EXTRA_LONGITUDE = "longitude";
	
	/**
	 * location parcelable extra that is returned when asking for a location
	 * using {@link MapsConstants.MIME_GPS}
	 */
	public static final String EXTRA_LOCATION = "location";
	
	/**
	 * Mime type for gps coordinates.
	 */
	public static final String MIME_GPS = "vnd.imogene.gestionflotteavion.maps/coordinates";
	
	/**
	 * Filter the available intents to ask for coordinates only using the sensor
	 */
	public static final String CATEGORY_GPS = "org.imogene.map.gestionflotteavion.category.GPS";

	/**
	 * Filter the available intents to ask for coordinates from the best sensor available
	 */
	public static final String CATEGORY_BEST = "org.imogene.map.gestionflotteavion.category.BEST";

	/**
	 * Filter the available intents to ask for coordinates from the network
	 */
	public static final String CATEGORY_NETWORK = "org.imogene.map.gestionflotteavion.category.NETWORK";

	/**
	 * Filter the available intents to ask for coordinates picking a point on a map
	 */
	public static final String CATEGORY_MAP = "org.imogene.map.gestionflotteavion.category.MAP";
	
}
