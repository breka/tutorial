package org.imogene.android.util.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

public class FileUtils {

	private static final int maxAttempts = 100;

	/**
	 * Write the request result data incoming in the input stream into the
	 * output stream
	 * 
	 * @param is
	 *            the request result input stream
	 * @param out
	 *            the outputStream
	 * @param expectedLength
	 *            the data length expected
	 * @return 0 if done with success, -1 otherwise
	 */
	public static final int writeInFile(InputStream is, OutputStream out,
			long expectedLength) throws IOException {
		boolean finished = false;
		long bytesRead = 0;
		int nbAttemps = 0;

		while (!finished) {
			/* while there is data to read, we read it */
			byte[] buffer = new byte[1024];

			int i;
			while ((i = is.read(buffer)) != -1) {
				if (i < 1024) {
					out.write(buffer, 0, i);
				} else {
					out.write(buffer);
				}
				bytesRead += i;
				nbAttemps = 0;
			}
			/* is the reading process finished ? */
			finished = ((bytesRead >= expectedLength && expectedLength != -1) || ++nbAttemps > maxAttempts);

			/* if not finish we wait and we retry */
			if (!finished) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					Log.e(FileUtils.class.getName(), "writeInFile error", ex);
				}
			} else {
				out.flush();
				out.close();
				is.close();
				if (bytesRead >= expectedLength)
					return 0;
				else
					return -1;
			}
		}
		/* return 0, if read all data with success, -1 otherwise */
		if (bytesRead >= expectedLength)
			return 0;
		else
			return -1;
	}
	
	public static final boolean deleteFile(String path) {
		File file = new File(path);
		return file.delete();
	}

	public static String getExtension(String uri) {
		if (uri == null) {
			return null;
		}

		int dot = uri.lastIndexOf(".");
		if (dot >= 0) {
			return uri.substring(dot);
		} else {
			// No extension.
			return "";
		}
	}
	
	public static void appendFile(ContentResolver r, Uri from, Uri to) throws IOException {
		InputStream is = r.openInputStream(from);
		OutputStream os = r.openOutputStream(to);
		byte[] b = new byte[4096];
		while (is.read(b) != -1) {
			os.write(b);
		}
		os.flush();
		os.close();
		is.close();
	}
	
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0o";
	    final String[] units = new String[] { "o", "Ko", "Mo", "Go", "To" };
	    int digitGroups = (int) (Math.log(size)/Math.log(1000));
	    return new DecimalFormat("#,##0.###").format(size/Math.pow(1000, digitGroups)) + " " + units[digitGroups];
	}
	
	/**
	 * Return the size of a directory in bytes
	 */
	public static long getDirectorySize(File dir) {
	    long result = 0;
	    File[] list = dir.listFiles();

	    for(int i = 0; i < list.length; i++) {
	        // Recursive call if it's a directory
	        if (list[i].isDirectory()) {
	            result += getDirectorySize(list[i]);
	        } else {
	            // Sum the file size in bytes
	            result += list[i].length();
	        }
	    }
	    return result; // return the file size
	}
	
	/**
	 * Remove directory
	 */
	public static void deleteDirectory(File dir) {
		File[] list = dir.listFiles();
		
		for (int i = 0; i < list.length; i++) {
			if (list[i].isDirectory()) {
				deleteDirectory(list[i]);
			} else {
				list[i].delete();
			}
		}
	}

}
