package org.imogene.admin.contrib;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.imogene.studio.contrib.ImogeneModelNature;
import org.imogene.studio.contrib.interfaces.GenerationManager;
import org.imogene.studio.contrib.interfaces.IconCopyTask;


public class AdminIconCopyTask implements IconCopyTask {

	@Override
	public void copyIcons(GenerationManager manager) throws CoreException {
		ImogeneModelNature mmn = (ImogeneModelNature) manager.getSelectedProject().getNature(ImogeneModelNature.ID);
		String imageFolder = "src/org/imogene/" + mmn.getModelName().toLowerCase() + "/public/images";
		IFolder destinationFolder = manager.getGeneratedProject().getFolder(imageFolder);
		File dir = new File(destinationFolder.getLocation().toOSString());
		if (!dir.exists()) {
			dir.mkdirs();
			manager.getGeneratedProject().refreshLocal(IResource.DEPTH_INFINITE, null);
		}
		for (IResource res : mmn.getIconsFolder().members())
			res.copy(destinationFolder.getFullPath().append(res.getName()),	true, null);
	}

}
