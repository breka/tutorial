package org.imogene.tools.androidhelper.builders;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.Signature;

public class WJavaBuilder extends IncrementalProjectBuilder {
	
	private static final String R_FILE_PATH_KEY = "pathToRFile";
	private static final String W_FILE_PATH_KEY = "pathToWFile";
	private static final String RIMPORT_KEY = "importRClass";
	private static final String WPKG_KEY = "packageWClass";

	private String mPathToRFile;
	private String mPathToWFile;
	private String mImportRClass;
	private String mPackageWClass;

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		mImportRClass = (String) args.get(RIMPORT_KEY);
		mPackageWClass = (String) args.get(WPKG_KEY);
		mPathToRFile = (String) args.get(R_FILE_PATH_KEY);
		mPathToWFile = (String) args.get(W_FILE_PATH_KEY);

		if (shoudAudit(kind))
			ResourcesPlugin.getWorkspace().run(new MyRunnable(), monitor);
		return null;
	}
	
	private boolean shoudAudit(int kind) {
		if (kind == FULL_BUILD)
			return true;
		IResourceDelta delta = getDelta(getProject());
		if (delta == null)
			return false;
		IResourceDelta[] children = delta.getAffectedChildren();
		for (int i = 0; i < children.length; i++) {
			String fileName = children[i].getProjectRelativePath().lastSegment();
			if ("gen".equals(fileName))
				return true;
		}
		return false;
	}

	private class MyRunnable implements IWorkspaceRunnable {
		
		@Override
		public void run(IProgressMonitor monitor) throws CoreException {
			try {
				createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
			ICompilationUnit in = JavaCore.createCompilationUnitFrom(getProject().getFile(mPathToRFile));
			ICompilationUnit out = JavaCore.createCompilationUnitFrom(getProject().getFile(mPathToWFile));
			for (IJavaElement element : in.getChildren()) {
				
				if (element.getElementType() == IJavaElement.TYPE) {
					IType type = (IType) element;
					IType root = out.createType("public final class W {}", null, true, monitor);
					for (IJavaElement e : type.getChildren()) {
						if (e.getElementType() == IJavaElement.TYPE) {
							IType subType = (IType) e;
							IType subRoot = root.createType("public static final class "+subType.getElementName()+" {}", null, true, monitor);
							for (IJavaElement subElement : subType.getChildren()) {
								if (subElement.getElementType() == IJavaElement.FIELD) {
									subRoot.createField(createContents((IField) subElement, subRoot), null, true, monitor);
								}
							}
						}
					}
				} else if (element.getElementType() == IJavaElement.PACKAGE_DECLARATION) {
					out.createPackageDeclaration(mPackageWClass, monitor);
					
					out.createImport(mImportRClass, null, monitor);
				}
			}
			in.close();
			out.close();
		}

		private void createNewFile() throws IOException {
			File file = new File(getProject().getLocation().toFile(), mPathToWFile);
			if (file.exists())
				file.delete();
			file.createNewFile();
		}
		
		private String createContents(IField field, IType parent) throws CoreException {
			return Flags.toString(field.getFlags())+" "+Signature.toString(field.getTypeSignature())+" "+field.getElementName()+" = R."+parent.getElementName()+"."+field.getElementName()+";";
		}

	}

}
