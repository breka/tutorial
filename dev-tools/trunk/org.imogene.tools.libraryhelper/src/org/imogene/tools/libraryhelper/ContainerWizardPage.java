package org.imogene.tools.libraryhelper;


import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.imogene.library.LibraryDesc;
import org.imogene.library.LibraryPlugin;

public class ContainerWizardPage extends NewElementWizardPage implements IClasspathContainerPage,  IClasspathContainerPageExtension {	
	
	private IClasspathEntry entryResult;
	
	public ContainerWizardPage(){
		super("Imogene Web application library");
		setTitle("Imogene Web Library");
		setDescription("Set of jars that composes the Imogene libraries");		
		entryResult = JavaCore.newContainerEntry(new Path(ClassContainerInitializer.LIBRARY_ID));		
	}
			
	@Override
	public void initialize(IJavaProject pProject,
			IClasspathEntry[] currentEntries) {				
	}

	@Override
	public boolean finish() {		
		return true;
	}

	@Override
	public IClasspathEntry getSelection() {			
		return entryResult;
	}

	@Override
	public void setSelection(IClasspathEntry containerEntry) {
		if(containerEntry!=null)
			System.out.println("setSelection entry: "+containerEntry.hashCode());		
	}

	@Override
	public void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout());
		Label label = new Label(mainComposite, SWT.NONE);
		label.setText("Imogene library for the web applications generated.");
		Label descTitle = new Label(mainComposite, SWT.NONE);
		descTitle.setText("Available libraries:");
		for(LibraryDesc desc : LibraryPlugin.getDefault().getAvailableLibraries()){
			Label dLabel = new Label(mainComposite, SWT.NONE);
			dLabel.setText(desc.getId());
		}
		setControl(mainComposite);
	}

	@Override
	public boolean isPageComplete() {		
		return super.isPageComplete();
	}
		

}
