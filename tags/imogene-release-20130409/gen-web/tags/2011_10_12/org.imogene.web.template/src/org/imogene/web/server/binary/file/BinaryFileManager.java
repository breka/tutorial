package org.imogene.web.server.binary.file;


/**
 * Singleton to manage the binary files, 
 * to get the binary file directory
 * @author MEDES-IMPS
 */
public class BinaryFileManager {
	
	private static BinaryFileManager instance = new BinaryFileManager();
	
	private String binary_file_dir;		
	
	/**
	 * Get the BinaryFile manager
	 */
	public static BinaryFileManager getInstance(){
		return instance;
	}

	/**
	 * Setter for bean injection
	 * @param binary_file_dir
	 */
	public void setBinary_file_dir(String binary_file_dir) {
		this.binary_file_dir = binary_file_dir;
	}
	
	
	/**
	 * Build a binary file path
	 * @param entityId binary id
	 * @param fileName file name
	 * @return absolute file path
	 */
	public String buildFilePath(String entityId, String fileName) {	
		return binary_file_dir + entityId + "-" + fileName;
	}
	
	
	

}
