package org.imogene.web.contrib.builder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.imogene.studio.contrib.tools.FileHelper;

import com.google.gwt.eclipse.core.runtime.GWTRuntime;

@SuppressWarnings("restriction")
public class GWTJarBuilder extends IncrementalProjectBuilder {
	
	private static final String CTX_OUT="war"; 

	@SuppressWarnings( { "unchecked" })
	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		/*IJavaModel root = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		IJavaProject project = root.getJavaProject(getProject().getName());*/
		IJavaProject project = JavaCore.create(getProject());
		System.out.println("Running the builder");
		GWTRuntime runtime = GWTRuntime.findSdkFor(project);
		for (File f : Arrays.asList(runtime.getWebAppClasspathFiles())) {		
			IFolder library = (IFolder)project.getProject().findMember(CTX_OUT+"/WEB-INF/lib/");			
			File folder = new File(library.getLocation().toOSString());
			File lib = new File(folder, f.getName());
			System.out.println("Src: "+f.getAbsolutePath());
			System.out.println("Dest: "+lib.getAbsolutePath());
			System.out.println("Should copy: "+shouldCopy(f, lib));
			if(shouldCopy(f, lib)){
				try{
					
					FileHelper.copyFile(f, new File(folder, f.getName()));
				}catch (IOException ioe) {
					ioe.printStackTrace();
				}
				project.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);				
			}
		}		

		return null;
	}
	
	private boolean shouldCopy(File sdk, File lib){
		if(lib.exists())
			return false;
		if(sdk.length()==lib.length())
			return false;
		return true;
	}

}
