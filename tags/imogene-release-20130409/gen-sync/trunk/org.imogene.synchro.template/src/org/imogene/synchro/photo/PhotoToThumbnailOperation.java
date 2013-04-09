package org.imogene.synchro.photo;

import java.io.File;
import java.util.Properties;

import org.imogene.sync.server.binary.BinaryOperation;
import org.imogene.sync.server.binary.file.BinaryFileManager;

public class PhotoToThumbnailOperation implements BinaryOperation {
	
	
	public void operate(File incoming) {
		throw new RuntimeException("Not impplemented use operate(File, Properties).");
	}
	
	public void operate(File incoming, Properties params){
		String mimetype = params.getProperty("mimetype");
		String binaryPath = BinaryFileManager.getInstance().GetBinary_file_dir();		
		if (mimetype.contains("image")) {
			File output = new File(binaryPath+"/"
					+ "thumb_"+incoming.getName());			
			PhotoConverter.convert(incoming, output, mimetype);		
		}	
	}		
	
}
