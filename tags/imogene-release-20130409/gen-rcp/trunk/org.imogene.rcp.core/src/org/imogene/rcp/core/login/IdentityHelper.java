package org.imogene.rcp.core.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.imogene.encryption.EncryptionManager;


/**
 * @author MEDES-IMPS
 */
public class IdentityHelper {

	public static String KEY_ID = "imog.ident.id";	
	public static String KEY_NAME = "imog.ident.name";	
	public static String KEY_FIRSTNAME = "imog.ident.firstname";	
	public static String KEY_LOGIN = "imog.ident.login";
	public static String KEY_PASSWORD = "imog.ident.password";
	public static String KEY_ROLES = "imog.ident.roles";
	
	/* constants used by the synchro server to send the current user role list to the client */
	public static String ROLE_SEPARATOR = ";";
	public static String ROLE_ERROR = "-ERROR-";
	
	/**
	 * Loads an identity from a file.
	 * @param idFile the identity file
	 * @return the identity describe by the file
	 */
	public static Identity loadFromFile(File idFile){
		EncryptionManager encrypter = EncryptionManager.getInstance();
		Properties properties = new Properties();
		Identity ident = null;
		try{
			properties.load(encrypter.getDecryptedInputStream(new FileInputStream(idFile)));
			ident=new Identity();
			ident.setId(properties.getProperty(KEY_ID));
			ident.setName(properties.getProperty(KEY_NAME));
			ident.setFirstName(properties.getProperty(KEY_FIRSTNAME));
			ident.setLogin(properties.getProperty(KEY_LOGIN));
			ident.setPassword(properties.getProperty(KEY_PASSWORD));
			ident.setAssignedRoles(getRoles(properties.getProperty(KEY_ROLES)));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ident;
	}
	
	/**
	 * Check if the loaded file is a valid identity file.
	 * @param idFile the identity file to check
	 * @return true if the file is valid.
	 */
	public static boolean validateIdFile(File idFile){
		EncryptionManager encrypter = EncryptionManager.getInstance();
		Properties properties = new Properties();
		try{
		properties.load(encrypter.getDecryptedInputStream(new FileInputStream(idFile)));
		if(properties.containsKey(KEY_ID)
				&& properties.containsKey(KEY_LOGIN)
				&& properties.containsKey(KEY_PASSWORD)
				&& properties.containsKey(KEY_ROLES)){
			return true;
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Copy files.
	 * @param f1 source file
	 * @param f2 destination file
	 */
	public static void copyfile(File f1, File f2){
		    try{
		     
		      InputStream in = new FileInputStream(f1);		      
		      OutputStream out = new FileOutputStream(f2);

		      byte[] buf = new byte[1024];
		      int len;
		      while ((len = in.read(buf)) > 0){
		        out.write(buf, 0, len);
		      }
		      in.close();
		      out.close();
		    }
		    catch(Exception ex){
		      ex.printStackTrace();
		    }
	}
	
	/**
	 * Gets a set of role ids from a string concatenation of role ids
	 * @param roleList a concatenated list of roles as sent and formated by the synchro server
	 * @return a set of roles
	 */
	private static Set<String> getRoles(String roleList) {
		Set<String> result = new HashSet<String>();
		if (roleList!=null && roleList.length()>0) {
			String[] roles = roleList.split(ROLE_SEPARATOR);
			for(String role:roles) {
				if (role.length()>0) {
					if (role.equals(ROLE_ERROR))
						return result;
					else
						result.add(role);
				}
			}
		}
		return result;
	}
}
