package org.imogene.rcp.core.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import org.imogene.common.data.handler.BeanKeyGenerator;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.sync.client.binary.BinaryFile;
import org.imogene.sync.client.binary.BinaryFileHandler;
import org.imogene.sync.client.binary.BinaryFileManager;

/**
 * This class provides static method that helps to handle binary data.
 * @author MEDES-IMPS
 *
 */
public class BinaryHelper {

	
	/**
	 * Include the file at the specified path, to the binary repository.
	 * @param path The path to the file to include
	 */
	public static File includeBinary(String path){
		File dest = null;
		try {			
			File binary = getBinaryDirectory();
			dest = new File(binary.getAbsolutePath() + "/" + UUID.randomUUID()
					+ getExtension(path));
			InputStream in = new FileInputStream(new File(path));
			OutputStream out = new FileOutputStream(dest);
			copyFile(in, out);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dest;
	}
	
	public static String addBinary(String entityId, String entityShortName, String entityFieldGetter, String entityAttachedBinaryId, File newBinary) {
		
		ImogPlugin plugin = ImogPlugin.getDefault();
		
		BinaryFileHandler binaryFileHandler = (BinaryFileHandler) plugin.getDataHandlerManager().getHandler(BinaryFile.class.getName());
		
		/* compare new binary to already attached binary */
		if (entityAttachedBinaryId != null && !entityAttachedBinaryId.equals("")) {
			BinaryFile entityAttachedBinary = (BinaryFile) binaryFileHandler.loadEntity(entityAttachedBinaryId, null);
			
			if (entityAttachedBinary != null && entityAttachedBinary.getFileName() != null && newBinary!=null) {
				String entityAttachedBinaryFileName = BinaryFileManager.getInstance().buildFileName(entityAttachedBinaryId, entityAttachedBinary.getFileName());
				if (entityAttachedBinaryFileName.equals(newBinary.getName()) && entityAttachedBinary.getLength() == newBinary.length()) {
					// same binary attached to existing parent
					return null;
				}
			}
			// new binary attached to existing parent - delete old binary
			binaryFileHandler.delete(entityAttachedBinary, null);
		}
		
		String binaryId = null;
		if (newBinary!=null) {
			
			/* save binary in DB */
			BinaryFile binary = new BinaryFile();

			binaryId = BeanKeyGenerator.getNewId("BIN");
			binary.setId(binaryId);
			binary.setCreated(new Date());
			binary.setCreatedBy(plugin.getUserParameters().getLogin());
			binary.setModified(new Date(System.currentTimeMillis()));	
			binary.setModifiedBy(plugin.getUserParameters().getLogin());
			binary.setModifiedFrom(ImogPlugin.getDefault().getTerminalId().toString());
			//binary.setUploadDate(new Date(System.currentTimeMillis()));

			//binary.setContentType(newBinary.);
			binary.setFileName(newBinary.getName());
			binary.setLength(newBinary.length());

			binary.setParentEntity(entityShortName);
			binary.setParentKey(entityId);
			binary.setParentFieldGetter(entityFieldGetter);

			binaryFileHandler.saveOrUpdate(binary, null);


			/* create binary File */
			File resultFile = new File(BinaryFileManager.getInstance()
					.buildFilePath(binaryId, newBinary.getName()));		

			try {
				copyFile(new FileInputStream(newBinary), new FileOutputStream(resultFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}					
		}
		return binaryId;
	}	
	
	/**
	 * Get the file with the given Id.
	 * @param binaryId The binaryId
	 */
	public static BinaryFile getBinary(String binaryId){
		
		BinaryFileHandler binaryFileHandler = (BinaryFileHandler) ImogPlugin.getDefault()
		.getDataHandlerManager().getHandler(BinaryFile.class.getName());
		
		BinaryFile binaryFile = (BinaryFile) binaryFileHandler.loadEntity(binaryId, null);

		return binaryFile;
	}
	
	/**
	 * Copy data from the input stream to the output stream using a buffer.
	 * @param in The input stream
	 * @param out The output stream
	 */
	private static void copyFile(InputStream in, OutputStream out) throws IOException{
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}
	
	/**
	 * Copy the binary data identified by its filename to the specified destination.
	 * @param filename The name of the binary file to extract
	 * @param path The path where to copy the file
	 */
	public static void extractBinary(String filename, String path){
		try {
			File binary = getBinaryDirectory();
			File src = new File(binary.getAbsoluteFile() + "/" + filename);
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(new File(path));
			copyFile(in, out);
		} catch (Exception ex) {
			ex.printStackTrace();
		}				
	}
	
	/**
	 * Return the extension of the file
	 * 
	 * @param pathToFile
	 *            the path to the file
	 * @return the file extension.
	 */
	private static String getExtension(String pathToFile){
		String extension = new String();
		int pointIndex = pathToFile.lastIndexOf(".");
		if(pointIndex>-1){
			extension = pathToFile.substring(pointIndex);
		}
		return extension;		
	}
	
	/**
	 * Get the Directory where are stored the binary data file.
	 * @return the directory that contained the binary data
	 */
	public static File getBinaryDirectory(){
		File binary = new File(BinaryFileManager.getInstance().GetBinary_file_dir());
		if (!binary.exists()) {
			binary.mkdir();
		}
		return binary;
	}
	
	
	
}
