package org.imogene.oaw.generator.dao;

import java.security.SecureRandom;
import java.util.Random;

public class DaoUtil {
	
	public static final String generateSerialVersionUID() {
		Random random = new Random();
		return Long.toString(random.nextLong());
	}

}
