package org.imogene.testsynchro.onsil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class TestTools {
	
	private static Logger logger = Logger.getLogger("org.imogene.generated.test.onsil.TestTools");

	public static void copyFile(String baseName, String id, String fileName){
		String src = J2SETest.BINARY_PATH + baseName;
		String dest = J2SETest.BINARY_PATH + id + "-"+ fileName;
		copyfile(src, dest);
	}
	
	private static void copyfile(String srFile, String dtFile){
	    try{
	      File f1 = new File(srFile);
	      File f2 = new File(dtFile);
	      InputStream in = new FileInputStream(f1);	      
	      OutputStream out = new FileOutputStream(f2);

	      byte[] buf = new byte[1024];
	      int len;
	      while ((len = in.read(buf)) > 0){
	        out.write(buf, 0, len);
	      }
	      in.close();
	      out.close();
	      //System.out.println("File copied.");
	    }
	    catch(FileNotFoundException ex){
	      logger.error(ex.getMessage());
	      System.exit(0);
	    }
	    catch(IOException e){
	    	logger.error(e.getMessage());      
	    }
	  }
}
