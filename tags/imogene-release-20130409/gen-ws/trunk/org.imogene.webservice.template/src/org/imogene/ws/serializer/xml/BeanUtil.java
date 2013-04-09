package org.imogene.ws.serializer.xml;

import java.util.Date;

import org.apache.log4j.Logger;
import org.imogene.ws.entity.MedooBean;
import org.imogene.ws.sync.SyncConstants;


public class BeanUtil {
	
	private static Logger logger = Logger.getLogger("org.imogene.ws.util.BeanUtil");
	

	/**
	 * Creates a new MedooBean instance with prefilled synchronisation properties
	 * @param id MedooBean id
	 * @param beanClass type of MedooBean
	 * @return a new MedooBean subclass instance
	 */
	public static MedooBean createNewEntity(String id, Class<?> beanClass) {
		//TODO handle  with not null constraint values
		MedooBean entity = null;
		try {
			entity = (MedooBean)beanClass.newInstance();
			entity.setId(id);
			entity.setCreationDate(new Date());
			entity.setLastModificationDate(new Date());
			entity.setCreator(SyncConstants.WS_ID_SYS);
			entity.setModifier(SyncConstants.WS_ID_SYS);
			entity.setModifiedFrom(SyncConstants.SYNC_ID_SYS);
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return entity;
	}

}
