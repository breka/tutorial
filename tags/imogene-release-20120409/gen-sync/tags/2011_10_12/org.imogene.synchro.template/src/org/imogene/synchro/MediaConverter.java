package org.imogene.synchro;

import java.io.File;

public interface MediaConverter {

	public String convert(File input, File output, String mimeType);
}
