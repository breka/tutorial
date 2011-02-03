package org.imogene.rcp.core.backup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.imogene.encryption.EncryptionManager;
import org.imogene.rcp.core.ImogPlugin;


public class SecureBackup {
	
	/**
	 * Backup data as encrypted files on the file system.
	 * @param cardId the entity id
	 * @param properties the properties of the entity
	 * @return true if success false otherwise
	 */
	public static boolean secureCard(String cardId, Object toSave){
		boolean isEncrypted=ImogPlugin.getDefault().isBackupEncrypted();
		boolean isActivated=ImogPlugin.getDefault().IsBackupActivated();
		if (isActivated) {
			String backupPath = ImogPlugin.getDefault().getBackupPath();
			File backupFile = new File(backupPath, cardId + ".backup");
			if (backupFile.exists())
				backupFile.delete();
			System.out.println(backupFile.getAbsolutePath());
			try {
				Properties properties = getProperties(toSave);
				FileOutputStream output = new FileOutputStream(backupFile);
				if (!isEncrypted)
					properties.store(output, "backup imogene");
				else {
					OutputStream encOut = EncryptionManager.getInstance()
							.getEncryptedOutputStream(output);
					properties.store(encOut, "backup imogene");
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	private static Properties getProperties(Object cs){
		Properties p = new Properties();		
		try{
			Map<?, ?> test = BeanUtils.describe(cs);
			cleanMap(test);
			p.putAll(test);
		}catch(Exception e){
			e.printStackTrace();
		}		
		return p;
	}
	
	@SuppressWarnings("unchecked")
	private static void cleanMap(Map  toClean){
		for(Object key : toClean.keySet()){			
			Object value = toClean.get(key);
			if(value==null)
				toClean.put(key, "");
		}
	}

}
