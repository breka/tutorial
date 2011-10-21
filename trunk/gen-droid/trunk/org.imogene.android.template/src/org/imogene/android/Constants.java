package org.imogene.android;

import java.io.File;

import android.os.Environment;

public final class Constants {
	
	public static final String AUTHORITY = "org.imogene.android.%realProjectName%.entity";
	
	public static interface Paths {
		public static final File PATH_BACKUP = new File(Environment.getExternalStorageDirectory(), ".%realProjectName%/backup");
		public static final File PATH_BINARIES = new File(Environment.getExternalStorageDirectory(), ".%realProjectName%/binaries");
		public static final File PATH_SYNCHRO = new File(Environment.getExternalStorageDirectory(), ".%realProjectName%/synchro");
		public static final File PATH_TEMPORARY = new File(Environment.getExternalStorageDirectory(), ".%realProjectName%/temporary");
		public static final File PATH_MEDIA = new File(Environment.getExternalStorageDirectory(), "%realProjectName%/media");
	}
	
	public static interface Intents {

		public static final String ACTION_SEARCH_ENTITY = "org.imogene.android.%realProjectName%.action.SEARCH_ENTITY";

		public static final String ACTION_CHECK_SYNC = "org.imogene.android.%realProjectName%.action.CHECK_SYNC";
		public static final String ACTION_RESCHEDULE = "org.imogene.android.%realProjectName%.action.RESCHEDULE";
		public static final String ACTION_CANCEL = "org.imogene.android.%realProjectName%.action.CANCEL";
		
		public static final String ACTION_SEND_FILTERS = "org.imogene.android.%realProjectName%.action.SEND_FILTERS";

		public static final String ACTION_RM_SYNC_HISTORY = "org.imogene.android.%realProjectName%.action.RM_SYNC_HISTORY";
		public static final String ACTION_RM_DATABASE = "org.imogene.android.%realProjectName%.action.RM_DATABASE";
		
		/* Outside intents */
		public static final String ACTION_SHOW_RADAR = "org.imogene.radar.SHOW_RADAR";
		
		public static final String ACTION_CAPTURE_GPS = "org.imogene.map.action.CAPTURE_GPS";

		public static final String ACTION_SHOW_ON_MAP = "org.imogene.map.action.SHOW_ON_MAP";

		public static final String ACTION_SHOW_CLOUDS = "org.imogene.map.action.SHOW_CLOUDS";
		
		public static final String ACTION_NEW_RECT = "org.imogene.map.action.NEW_RECT";
		
		public static final String ACTION_VIEW_RECT = "org.imogene.map.action.VIEW_RECT";
		
		public static final String ACTION_MANAGE_RECT = "org.imogene.map.action.MANAGE_RECT";
		
		public static final String ACTION_DOWNLOAD_TILES = "org.imogene.map.action.DOWNLOAD_TILES";
		
		public static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
		
		public static final String ACTION_SECRET_CODE = "android.provider.Telephony.SECRET_CODE";
		
		public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
		
		/* Notification */
		public static final String ACTION_NOTIFICATION_DELETED = "org.imogene.android.%realProjectName%.receiver.NOTIFICATION_DELETED";
	}
	
	public static interface Databases {
		public static final String DATABASE_NAME = "imogene.db";
	}

	public static interface Keys {
		/* Entity properties */
		public static final String KEY_ROWID = "_id";
		
		/* Actor properties */
		public static final String KEY_LOGIN = "login";
		public static final String KEY_PASSWORD = "password";
		public static final String KEY_ROLES = "assignedRoles";

		/* Binaries properties */
		public static final String KEY_LENGTH = "length";
		public static final String KEY_CONTENT_TYPE = "contentType";
		public static final String KEY_FILE_NAME = "fileName";
		public static final String KEY_DATA = "_data";
		public static final String KEY_PARENT_ENTITY = "parentEntity";
		public static final String KEY_PARENT_KEY = "parentKey";
		public static final String KEY_PARENT_FIELD_GETTER = "parentFieldGetter";

		/* Synchronizable properties */
		public static final String KEY_ID = "id";
		public static final String KEY_MODIFIED = "modified";
		public static final String KEY_UPLOADDATE = "uploadDate";
		public static final String KEY_MODIFIEDBY = "modifiedBy";
		public static final String KEY_MODIFIEDFROM = "modifiedFrom";
		public static final String KEY_CREATED = "created";
		public static final String KEY_CREATEDBY = "createdBy";
		public static final String KEY_DATE = "date";
		public static final String KEY_STATUS = "status";
		public static final String KEY_LEVEL = "level";

		/* gps location properties */
		public static final String KEY_ACCURACY = "accuracy";
		public static final String KEY_ALTITUDE = "altitude";
		public static final String KEY_BEARING = "bearing";
		public static final String KEY_LATITUDE = "latitude";
		public static final String KEY_LONGITUDE = "longitude";
		public static final String KEY_PROVIDER = "provider";
		public static final String KEY_SPEED = "speed";
		public static final String KEY_TIME = "time";
		public static final String KEY_HASACCURACY = "hasaccuracy";
		public static final String KEY_HASALTITUDE = "hasaltitude";
		public static final String KEY_HASBEARING = "hasbearing";
		public static final String KEY_HASSPEED = "hasspeed";

		/* private android properties */
		public static final String KEY_UNREAD = "unread";
		public static final String KEY_SYNCHRONIZED = "synchronized";
		
		/* client filter properties */
		public static final String KEY_USERID = "userId";
		public static final String KEY_TERMINALID = "terminalId";
		public static final String KEY_CARDENTITY = "cardEntity";
		public static final String KEY_ENTITYFIELD = "entityField";
		public static final String KEY_OPERATOR = "operator";
		public static final String KEY_FIELDVALUE = "fieldValue";
		public static final String KEY_DISPLAY = "display";
		public static final String KEY_ISNEW = "isNew";
		
		/* Translatable text entity properties */
		public static final String KEY_TRANSLATIONS = "translations";
		public static final String KEY_FIELD_ID = "fieldId";
		public static final String KEY_LOCALE = "locale";
		public static final String KEY_VALUE = "value";
		public static final String KEY_ORIGINAL_VALUE = "originalValue";
		public static final String KEY_POTENTIALY_WRONG = "potentialyWrong";
	}

	public static interface Tables {
		/* private android tables */
		public static final String TABLE_GPSLOCATIONS = "gpslocation";
		public static final String TABLE_SYNCHISTORY = "synchistory";
	}

	public static interface Sync {
		public static final String SYNC_SYSTEM = "sync-system";
	}

	public static interface Status {
		public static final int STATUS_OK = 0;
		public static final int STATUS_ERROR = 1;
	}

	public static interface Levels {
		public static final int LEVEL_SEND = 0;
		public static final int LEVEL_RECEIVE = 1;
	}

	public static interface Extras {

		/**
		 * String value for the AbstractEntityDownloader activity, should be an
		 * entity id (scanned or typed)
		 */
		public static final String EXTRA_SEARCH = "org.imogene.android.extra.SEARCH";

		/**
		 * The base or saved entity to pass to the entity editor
		 */
		public static final String EXTRA_ENTITY = "org.imogene.android.extra.ENTITY";

		/**
		 * Where clause to display an list of entities
		 */
		public static final String EXTRA_WHERE = "org.imogene.android.extra.WHERE";
		
		/**
		 * Had these extra to indicate that the list should allow multiple
		 * selection
		 */
		public static final String EXTRA_MULTIPLE = "org.imogene.android.extra.MULTIPLE";
		
		/**
		 * A {@link ArrayList<Uri>} of preselected entities to pass to a
		 * multiple selection list
		 */
		public static final String EXTRA_SELECTED = "org.imogene.android.extra.SELECTED";
		/**
		 * The key to sort the entities by
		 */
		public static final String EXTRA_SORT_KEY = "org.imogene.android.extra.SORT_KEY";
		
		/**
		 * The sort order 0 for descendant, 1 for ascendant
		 */
		public static final String EXTRA_SORT_ORDER = "org.imogene.android.extra.SORT_ORDER";

		/**
		 * Used when broadcasting with {@link Intents.ACTION_RM_DATABASE} to
		 * remove database without checking for unsynchronized entities
		 */
		public static final String EXTRA_FORCE = "org.imogene.android.extra.FORCE";
		
		
		/* Outside Extras */
		/**
		 * latitude extra (must be a float value) to use with
		 * {@link Intents.ACTION_SHOW_RADAR} or {@link Intents.ACTION_SHOW_ON_MAP}
		 */
		public static final String EXTRA_LATITUDE = "latitude";

		/**
		 * longitude extra (must be a float value) to use with
		 * {@link Intents.ACTION_SHOW_RADAR} or {@link Intents.ACTION_SHOW_ON_MAP}
		 */
		public static final String EXTRA_LONGITUDE = "longitude";
		
		public static final String EXTRA_ITEM_NUMBER = "itemNumber";
		
		public static final String EXTRA_ITEM_DESCRIPTIONS = "itemDescriptions";
		
		public static final String EXTRA_ITEM_TITLES = "itemTitles";
		
		public static final String EXTRA_ITEM_LATITUDES = "itemLatitudes";
		
		public static final String EXTRA_ITEM_LONGITUDES = "itemLongitudes";
		
		public static final String EXTRA_ITEM_URIS = "itemUris";

		public static final String EXTRA_LOCATION = "location";
		
		public static final String EXTRA_NORTH = "northLatitude";
		
		public static final String EXTRA_SOUTH = "southLatitude";
		
		public static final String EXTRA_EAST = "eastLongitude";
		
		public static final String EXTRA_WEST = "westLongitude";
		
		public static final String EXTRA_BOXES_BUNDLE = "boxesBundle";
		
		public static final String EXTRA_BOXES_COUNT = "boxesCount";
		
		public static final String EXTRA_ENABLED_BOXES = "enabledBoxes";
		
		public static final String EXTRA_EDITABLE_BOXES = "editableBoxes";
		
		public static final String EXTRA_NORTH_LATITUDES = "northLatitudes";
		
		public static final String EXTRA_EAST_LONGITUDES = "eastLongitudes";
		
		public static final String EXTRA_SOUTH_LATITUDES = "southLatitudes";
		
		public static final String EXTRA_WEST_LONGITUDES = "westLongitudes";
	}

	public static interface SortOrder {
		/**
		 * Descendant order to display entities
		 */
		public static final int DESCENDANT_ORDER = 0;
		/**
		 * Descendant order to display entities
		 */
		public static final int ASCENDANT_ORDER = 1;
	}
	
	public static interface Categories {
		
		public static final String CATEGORY_CLASSIC = "org.imogene.android.category.CLASSIC";
		public static final String CATEGORY_WIZARD = "org.imogene.android.category.WIZARD";
		
		public static final String CATERGORY_IMAGE_CAPTURE = "org.imogene.android.diabsat.category.IMAGE_CATPURE";
		public static final String CATERGORY_VIDEO_CAPTURE = "org.imogene.android.diabsat.category.VIDEO_CATPURE";
		
		/* Outside categories */
		public static final String CATEGORY_GPS = "org.imogene.map.category.GPS";

		public static final String CATEGORY_BEST = "org.imogene.map.category.BEST";

		public static final String CATEGORY_NETWORK = "org.imogene.map.category.NETWORK";

		public static final String CATEGORY_MAP = "org.imogene.map.category.MAP";
		
	}
	
	public static interface Packages {
		public static final String PACKAGE_RADAR = "com.google.android.radar";

		public static final String PACKAGE_ZXING = "com.google.zxing.client.android";

		public static final String PACKAGE_MAP = "org.imogene.map";
	}
	
	public static interface Editor {
		public static final String ERROR_TAG = "errorTag";
		public static final String ERROR_LAYOUT = "errorLayout";
		public static final String ERROR_MSG = "errorMsg";
		public static final String ERROR_DESC = "errorDesc";
	}
	
	public static interface Viewer {
		public static final String INFO_DESC = "infoDesc";
		public static final String INFO_VALUE = "infoValue";
	}

}
