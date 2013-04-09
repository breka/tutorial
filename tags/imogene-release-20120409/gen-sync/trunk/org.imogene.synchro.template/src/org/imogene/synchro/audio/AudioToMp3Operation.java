package org.imogene.synchro.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.imogene.sync.server.binary.BinaryOperation;
import org.imogene.sync.server.binary.file.BinaryFileManager;
import org.imogene.synchro.MediaConverter;

public class AudioToMp3Operation implements BinaryOperation {

	private static final String MP3_DIR = "mp3";
	
	private MediaConverter audioConverter;
	
	public void operate(File incoming) {
		throw new RuntimeException("Not impplemented use operate(File, Properties).");
	}
	
	public void setAudioConverter(MediaConverter pConverter){
		audioConverter = pConverter;
	}
	
	public void operate(File incoming, Properties params){
		String mimetype = params.getProperty("mimetype");
		String binaryPath = BinaryFileManager.getInstance().GetBinary_file_dir();
		testFlvDir(binaryPath);
		if (mimetype.contains("audio")) {
			File output = new File(binaryPath + MP3_DIR + "/"
					+ incoming.getName() + ".mp3");
			if (!mimetype.contains("mp3")) {
				audioConverter.convert(incoming, output, mimetype);
			} 
			else {
				try{
				copy(new FileInputStream(incoming),
						new FileOutputStream(output));
				}catch(FileNotFoundException fnfe){
					fnfe.printStackTrace();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		}	
	}
	
	private void testFlvDir(String binaryPath){
		File mp3Dir = new File(binaryPath+MP3_DIR);
		if(!mp3Dir.exists())
			mp3Dir.mkdir();
	}
	
	/**
	 * Copy the binary file 
	 * @param in input stream
	 * @param out output stream
	 * @throws IOException
	 */
	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		try {
			byte[] buffer = new byte[1024];
			int nrOfBytes = -1;
			while ((nrOfBytes = in.read(buffer)) != -1) {
				out.write(buffer, 0, nrOfBytes);
			}
			out.flush();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
