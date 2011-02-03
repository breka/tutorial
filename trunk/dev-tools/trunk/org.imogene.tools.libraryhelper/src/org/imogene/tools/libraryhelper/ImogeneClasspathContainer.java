package org.imogene.tools.libraryhelper;

import java.util.List;
import java.util.Vector;

import org.eclipse.jdt.core.IClasspathEntry;
import org.imogene.library.LibraryDesc;

public class ImogeneClasspathContainer extends AbstractClasspathContainer {		
	
	private List<IClasspathEntry> entries  = new Vector<IClasspathEntry>();
	
	private String name;
	
	public ImogeneClasspathContainer(LibraryDesc library){		
		addJarsToEntries(library.getUrl());
		name = library.getName();
	}

	@Override
	protected List<IClasspathEntry> getEntries() {		
		return entries;
	}

	@Override
	public String getDescription() {		
		return name;
	}
	
}
