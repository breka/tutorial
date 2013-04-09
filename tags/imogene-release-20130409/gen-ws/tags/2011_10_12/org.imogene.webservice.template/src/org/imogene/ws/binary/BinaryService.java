package org.imogene.ws.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.imogene.ws.dao.GenericDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class BinaryService {

	private static final String DEFAULT_DIRECTORY = "/binaries/";	

	private String binaryPath = DEFAULT_DIRECTORY;
	public void setBinaryPath(String path){
		binaryPath = path;
	}
	
	private GenericDao binaryDao;	
	public void setGenericDao(GenericDao dao){
		binaryDao = dao;
	}	

	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value="binaries/{id}")
	public void getBinary(HttpServletResponse resp, @PathVariable String id){
		try{
		Binary binary = (Binary)binaryDao.loadEntity(BinaryFile.class, id);
		if (binary != null) {
			resp.setHeader("Content-Disposition", "attachment; filename=\""
					+ binary.getFileName() + "\"");
			resp.setContentType(binary.getContentType());
			resp.setContentLength((int) binary.getLength());
			copy(new FileInputStream(getLocalFile(binary)), resp
					.getOutputStream());
		} else {
			resp.sendError(404);
		}	
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	
	/**
	 * Get the local file where copy the FileItem
	 * @param remoteName the remote name 
	 * @return the corresponding file.
	 */
	private File getLocalFile(Binary binary) {
		return new File(binaryPath + binary.getId() + "-"
				+ binary.getFileName());
	}
	
	
	/**
	 * Copy the binary file to the http response output stream
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
