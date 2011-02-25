package org.imogene.sync.server.localizedtext;

import java.util.Date;
import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.common.data.handler.EntityHandlerImpl;
import org.imogene.sync.SyncConstants;
import org.imogene.sync.localizedtext.LocalizedText;
import org.imogene.uao.security.ImogSecurityHandler;

/**
 * Implements a data handler for the LocalizedText 
 * @author Medes-IMPS
 */
public class LocalizedTextHandler extends EntityHandlerImpl
		implements
			EntityHandler {

	private LocalizedTextDao dao;

	public Synchronizable createNewEntity(String id) {
		//TODO handle  with not null constraint values
		LocalizedText entity = new LocalizedText();
		entity.setId(id);
		entity.setModified(new Date());
		entity.setCreatedBy(SyncConstants.SYNC_ID_SYS);
		entity.setModifiedBy(SyncConstants.SYNC_ID_SYS);
		entity.setModifiedFrom(SyncConstants.SYNC_ID_SYS);
		return entity;
	}

	public void saveOrUpdate(Synchronizable entity, SynchronizableUser user) {
		LocalizedText toSave = (LocalizedText) ImogSecurityHandler
				.getInstance().getPolicy().toHibernate(entity, user);
		if (toSave != null) {
			getDao().saveOrUpdate(toSave);
		}

	}

	public void delete(Synchronizable entity, SynchronizableUser user) {
		Synchronizable toHibernate = ImogSecurityHandler.getInstance()
				.getPolicy().toHibernate(entity, user);
		// TODO deletion of foreign keys references
		if (toHibernate != null)
			getDao().delete(toHibernate);
	}

	protected ImogJunction createFilterJuntion(SynchronizableUser user) {
		ImogConjunction filterConjunction = new ImogConjunction();

		return filterConjunction;
	}

	/**
	 * Creates filtering criterias
	 * @param userId the login of the user whose filters are searched
	 * @param terminalId Id of the terminal for which filtering criterias are defined
	 * @return Meedoo junction containing filtering criterias if the user
	 * has created filtering criteria from the terminal whose id is passed as a parameter
	 */
	protected ImogJunction createClientFilterJuntion(String userId,
			String terminalId) {
		return null;
	}

	public List<Synchronizable> loadEntities(int startRow, int maxRows,
			String sortProperty, boolean sortOrder, SynchronizableUser user) {
		// not used in the context of the synchro server
		return null;
	}

	/**
	 * Setter for bean injection
	 * @param dao
	 */
	public void setDao(LocalizedTextDao dao) {
		this.dao = dao;
	}

	public void setDao(EntityDao dao) {
		this.dao = (LocalizedTextDao) dao;
	}

	public LocalizedTextDao getDao() {
		return dao;
	}

}
