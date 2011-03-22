package org.imogene.tools.libraryhelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.imogene.library.Constants;
import org.imogene.library.LibraryPlugin;
import org.imogene.library.Tools;

public abstract class AbstractClasspathContainer implements IClasspathContainer {	
	
	protected abstract List<IClasspathEntry> getEntries();		
			
	/*
	 * Add jars describe in the xml document
	 * to the project build path.
	 * @param xmlDescription url to xml description file
	 */
	public void addJarsToEntries(URL xmlDescription){
		Collection<URL> jars = new ArrayList<URL>(); 
		try {
			for (String name : Tools.getJarNames(xmlDescription.openStream())) {
				URL url = LibraryPlugin.getDefault().getBundle().getEntry(Constants.JAR_PATH+name);
				if(url!=null)
					jars.add(url);
				else
					throw new RuntimeException("Library named '"+name+"' not found.");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		addToEntries(jars);
	}
	
	/*
	 * add jars as classpath entries.
	 * @param jars The jars to add
	 */
	private void addToEntries(Collection<URL> jars){
		for(URL url : jars){		
			try{
				IPath path = new Path(FileLocator.toFileURL(url).getPath());
				getEntries().add(JavaCore.newLibraryEntry(path, null, new Path("/")));
			}catch(IOException ioe){
				ioe.printStackTrace();				
			}
		}
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {		
		return getEntries().toArray(new IClasspathEntry[0]);
	}	

	@Override
	public int getKind() {		
		return 0;
	}

	@Override
	public IPath getPath() {		
		return null;
	}
	
}