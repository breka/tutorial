package org.imogene.web.contrib.builder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.imogene.library.contrib.export.ExportManager;
import org.imogene.library.contrib.export.ExportedEntry;
import org.imogene.studio.contrib.tools.FileHelper;
import org.imogene.studio.contrib.ui.navigator.WebShadow;
import org.imogene.web.contrib.Activator;

import com.google.gwt.eclipse.core.runtime.GWTRuntime;

@SuppressWarnings("restriction")
public class GWTJarBuilder extends IncrementalProjectBuilder {
	
	
	private static String LIBRARY_PATH = "war/WEB-INF/lib/";
	
	private static final String COMPATIBILITY_EXTENSION="2.1-compatibility";
	
	@Override
	protected IProject[] build(int kind, @SuppressWarnings("rawtypes") Map args, IProgressMonitor monitor)
			throws CoreException {		
		IJavaProject project = JavaCore.create(getProject());		
		GWTRuntime runtime = GWTRuntime.findSdkFor(project);
		for (File f : Arrays.asList(runtime.getWebAppClasspathFiles())) {		
			//IFolder library = (IFolder)project.getProject().findMember(CTX_OUT+"/WEB-INF/lib/");
			IFolder library = (IFolder)project.getProject().findMember(LIBRARY_PATH);
			File folder = new File(library.getLocation().toOSString());
			File lib = new File(folder, f.getName());
			if(shouldCopy(f, lib)){
				try{					
					FileHelper.copyFile(f, new File(folder, f.getName()));
				}catch (IOException ioe) {
					ioe.printStackTrace();
				}
				project.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);				
			}
		}		
		gileadCompatibility(runtime.getVersion(), getProject());
		return null;
	}
	
	private boolean shouldCopy(File sdk, File lib){
		if(lib.exists())
			return false;
		if(sdk.length()==lib.length())
			return false;
		return true;
	}
	
	/**
	 * Set the Gilead compatibility checking the SDK version
	 * 
	 * @param sdkVersion
	 *            the GWT SDK version
	 * @param project
	 *            the WEB project
	 * @throws CoreException
	 */
	private void gileadCompatibility(String sdkVersion, IProject project)
			throws CoreException {	
		List<ExportedEntry> entries = ExportManager
				.getClasspath(WebShadow.NATURE);
		IFolder iDestination = project.getFolder(LIBRARY_PATH);
		for (ExportedEntry e : entries) {
			try {
				IFile iFile = iDestination.getFile(e.getFileName());
				if (needCompatibility(sdkVersion)) {
					if (!iFile.exists()
							&& e.getFileName()
									.contains(COMPATIBILITY_EXTENSION))
						iFile.create(e.openStream(), true, null);
				} else {
					if (iFile.exists()
							&& e.getFileName()
									.contains(COMPATIBILITY_EXTENSION))
						iFile.delete(true, null);
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
	}
	
	/**
	 * check if the project need the 'Gilead' compatibility library
	 * @param sdkVersion
	 * @return
	 */
	private boolean needCompatibility(String sdkVersion){		
		String[] versions = sdkVersion.split("\\.");
		if(versions.length<2){
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Invalid sdk version number: '"+sdkVersion+"'"));
			return false;			
		}else{
			try{
				if(Integer.parseInt(versions[0])>1){
					if(Integer.parseInt(versions[1])>1){
						return false;
					}else{
						return true;
					}
				}else{
					return true;
				}
			}catch(NumberFormatException nfe){
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Invalid sdk version number: "+nfe.getMessage(), nfe));
			}
		}
		return false;
	}

}
