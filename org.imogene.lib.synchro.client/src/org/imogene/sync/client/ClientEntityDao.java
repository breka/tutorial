package org.imogene.sync.client;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.data.handler.DataHandlerManager;

public interface ClientEntityDao extends EntityDao {

	public void setHandlerManager(DataHandlerManager manager);
}
