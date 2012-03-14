package org.imogene.rcp.core;

public class Constants {

	public final static String SYNC_ID_SYS = "-system-";
	
	public final static int SYNC_STATUS_OK = 0;
	
	public final static int SYNC_STATUS_CANCEL = 1;
	
	public final static int SYNC_STATUS_ERROR = 2;
	
	public final static String KEY_BACKUP_PATH="backup.path";
	public final static String BACUP_PATH_DEFAULT="/home/backup/";

	public static final String KEY_BACKUP_ACTIVATED = "backup.activation";
	public static final boolean BACKUP_ACTIVATED_DEFAULT = true;

	public static final String KEY_BACKUP_ENCRYPTION = "backup.encryption";
	public static final boolean BACKUP_ENCRYPTION_DEFAULT = true;
}
