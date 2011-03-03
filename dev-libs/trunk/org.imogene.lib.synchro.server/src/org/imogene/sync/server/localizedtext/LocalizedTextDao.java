package org.imogene.sync.server.localizedtext;

import java.util.Date;
import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.sync.localizedtext.LocalizedText;

/**
 * Implements a Hibernate DAO for the LocalizedText 
 * @author Medes-IMPS
 */
public interface LocalizedTextDao extends EntityDao {
	
	/**
	 * Load the entities of type LocalizedText for the field fieldId
	 * @param fieldId the id of the field for which localized texts should be gotten
	 * @return a list of localizedText
	 */
	public List<LocalizedText> listLocalizedText(String fieldId);

	public List<Synchronizable> loadEntities();

	public List<Synchronizable> loadEntities(ImogJunction criterions);

	public Synchronizable loadEntity(String entityId);

	public List<Synchronizable> loadModified(Date date);

	public List<Synchronizable> loadModified(Date date, ImogJunction criterions);

	public Synchronizable loadModified(Date date, String entityId);

	public Synchronizable loadModified(Date date, ImogJunction criterions,
			String entityId);

	public void saveOrUpdate(Synchronizable entity);

	public void merge(Synchronizable entity);

	public int countAll();

	public void delete(Synchronizable entity);

	public void deleteEntities();

	public void clear();

	public void flush();

}
