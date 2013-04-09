package org.imogene.tools.encrypter;

import java.io.InputStream;

import javax.crypto.SecretKey;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.imogene.encryption.EncryptionManager;
import org.imogene.encryption.EncryptionTools;
import org.imogene.encryption.KeyProvider;
import org.imogene.encryption.SecretKeyLoadingException;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements KeyProvider {

	// The plug-in ID
	public static final String PLUGIN_ID = "MedooIdEncrypter";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;		
		EncryptionManager.getInstance().setKeyProvider(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	@Override
	public SecretKey getKey() throws SecretKeyLoadingException {
		try{
			InputStream str = this.getBundle().getResource("key/encryption.key").openStream();			
			return EncryptionTools.loadSecretKeyFromStream(str);
		}catch (Exception e) {
			throw new SecretKeyLoadingException(e);
		}
	}
	
}
