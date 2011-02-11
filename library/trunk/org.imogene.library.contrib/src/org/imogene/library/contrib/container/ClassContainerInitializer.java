package org.imogene.library.contrib.container;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.imogene.studio.contrib.ui.navigator.AdminShadow;
import org.imogene.studio.contrib.ui.navigator.InitializerShadow;
import org.imogene.studio.contrib.ui.navigator.NotifierShadow;
import org.imogene.studio.contrib.ui.navigator.SynchroShadow;
import org.imogene.studio.contrib.ui.navigator.WebShadow;

public class ClassContainerInitializer extends
		ClasspathContainerInitializer{

	public final static String LIBRARY_ID="org.imogene.library.contrib.IMOGENE_LIB";
	
	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		
		IProject p = project.getProject();
		AbstractClasspathContainer con = null;
		//displayInfoAboutProject(p);				
		
		if(p.hasNature(NotifierShadow.NATURE)){
			con = new NotifierClasspathContainer();
		}
		else if(p.hasNature(SynchroShadow.NATURE)){
			con = new SynchroClasspathContainer();
		}
		else if(p.hasNature(WebShadow.NATURE)){
			con = new WebAdminClasspathContainer();
		}
		else if(p.hasNature(AdminShadow.NATURE)){
			con = new WebAdminClasspathContainer();
		}
		else if(p.hasNature(InitializerShadow.NATURE)){
			con = new InitializerClasspathContainer();
		}
		if(con==null)
			con = new AllClasspathContainer();
		
		if(con!=null){
			JavaCore.setClasspathContainer(containerPath,
					new IJavaProject[] { project },
					new IClasspathContainer[] { con }, null);
		}
		
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
	


}
