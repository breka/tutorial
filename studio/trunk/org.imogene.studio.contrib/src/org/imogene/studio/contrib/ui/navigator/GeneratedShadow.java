package org.imogene.studio.contrib.ui.navigator;

import org.eclipse.core.resources.IProject;
import org.imogene.studio.contrib.ImogeneStudioPlugin;


/**
 * Section that groups all generated 
 * projects for a Medoo model.
 * @author Medes-IMPS
 */
public class GeneratedShadow extends AbstractShadow {

	public static final String TYPE="generated"; //$NON-NLS-1$
	
	public GeneratedShadow(IProject parent){
		super(parent, TYPE);
		setLabel("Generated projects"); //$NON-NLS-1$
		setIcon(ImogeneStudioPlugin.getImageDescriptor(
			"icons/launch_web_16.gif").createImage()); //$NON-NLS-1$
	}
	
	@Override
	public Object[] getChildren() {
		IShadow[] pp = new IShadow[8];
		pp[0] = new InitializerShadow(parent);
		pp[1] = new WebShadow(parent);
		pp[2] = new SynchroShadow(parent);
		pp[3] = new RcpShadow(parent);
		pp[4] = new AndroidShadow(parent);
		pp[5] = new AdminShadow(parent);
		pp[6] = new NotifierShadow(parent);
		pp[7] = new WebServiceShadow(parent);
		return pp;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

}
