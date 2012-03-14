package org.imogene.synchro.audio;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.imogene.synchro.MediaConverter;


public class AudioConverter implements MediaConverter {
		
	private String commandLine=null;
	
	private Logger logger = Logger.getLogger("org.imogene.synchro.audio");
	
	public String convert(File input, File output, String mimeType){
		runCommand(input.getAbsolutePath(), output.getAbsolutePath());
		return null;
	}
	
	public void setCommandLine(String pCommandLine){
		commandLine = pCommandLine;
	}
	
	private void runCommand(String inFullPath, String outFullPath){
		try{
			if(commandLine==null || commandLine.equals(""))
				logger.warn("The audio to .mp3 converter is not set, see the 'application.propertes' file.");
			else{
				//String cmd = "ffmpeg -i "+inFullPath+" "+outFullPath;	
				String cmd = commandLine.replace("%IN%", inFullPath).replace("%OUT%", outFullPath);
				logger.debug("Audio conversion: "+cmd);
				Runtime.getRuntime().exec(cmd);
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	
}
