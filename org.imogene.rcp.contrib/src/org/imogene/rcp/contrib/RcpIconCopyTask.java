package org.imogene.rcp.contrib;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.imogene.studio.contrib.ImogeneModelNature;
import org.imogene.studio.contrib.interfaces.GenerationManager;
import org.imogene.studio.contrib.interfaces.IconCopyTask;


public class RcpIconCopyTask implements IconCopyTask {

	@Override
	public void copyIcons(GenerationManager manager) throws CoreException {
		ImogeneModelNature mmn = (ImogeneModelNature) manager.getSelectedProject().getNature(ImogeneModelNature.ID);
		IFolder destinationFolder = manager.getGeneratedProject().getFolder("icons");
		File dir = new File(destinationFolder.getLocation().toOSString());
		if (dir.exists()) {
			dir.delete();
		}
		manager.getGeneratedProject().refreshLocal(IResource.DEPTH_INFINITE, null);
		mmn.getIconsFolder().copy(destinationFolder.getFullPath(), true, null);
	}

}
