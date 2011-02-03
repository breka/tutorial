package org.imogene.tools.libraryhelper;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.imogene.library.LibraryDesc;
import org.imogene.library.LibraryPlugin;

public class ClassContainerInitializer extends
		ClasspathContainerInitializer{

	public final static String LIBRARY_ID="org.imogene.tools.libraryhelper.IMOGENE_DEV_LIB";
	
	
	
	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		
		IProject p = project.getProject();
		AbstractClasspathContainer con = null;
		displayInfoAboutProject(p);								
		try{			
			//debug("Library url: "+lib);
			con = new ImogeneClasspathContainer(getLibrary(p));
			JavaCore.setClasspathContainer(containerPath,
					new IJavaProject[] { project },
					new IClasspathContainer[] { con }, null);
		}catch (Exception e) {	
			if(Display.getCurrent()!=null && Display.getCurrent().getActiveShell()!=null){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error adding the container path", 
					"An error occured adding this library, maybe you forgot to add the appropriate nature to your project.\n Error caused by: "+e.getMessage());
			}
			e.printStackTrace();
		}
	}		
	
	private LibraryDesc getLibrary(IProject project) throws CoreException {
		List<LibraryDesc> libs = LibraryPlugin.getDefault().getAvailableLibraries();		
		for(LibraryDesc lib : libs){			
			if(project.hasNature(lib.getId()))
					return lib;
		}
		return null;
	}
	
	private void displayInfoAboutProject(IProject p){
		if(p!=null){
			System.out.println("----- LIB project info -----");
			System.out.println("Name: "+p.getName());
			try{
				for(String id : p.getDescription().getNatureIds()){
					System.out.println("Nature: "+id);
				}
			}catch(CoreException ce){
				ce.printStackTrace();
			}
		}else{
			System.out.println("Project is null");
		}
	}
	
	/*private void debug(String message){
		MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Debug", 
		message);
	}*/


}
