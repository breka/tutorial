package org.imogene.ws.dao;

import java.util.List;

import org.imogene.ws.criteria.MedooJunction;
import org.imogene.ws.entity.LocalizedText;

/**
 * Implements a Hibernate DAO for the LocalizedText 
 * @author Medes-IMPS
 */
public interface LocalizedTextDao {
	
	/**
	 * Load the entities of type LocalizedText for the field fieldId
	 * @param fieldId the id of the field for which localized texts should be gotten
	 * @return a list of localizedText
	 */
	public List<LocalizedText> listLocalizedText(String fieldId);
	
	/**
	 * Load the entity with the specified id
	 * @param entityId the entity id
	 * @return the entity or null
	 */
	public LocalizedText getLocalizedText(String id);

	/**
	 * Load the entities with the specified ids
	 * @return list of entities with the specified ids
	 */
	public List<LocalizedText> listLocalizedText(List<String> ids);

	/**
	 * Load the entity with the specified id
	 * @param entityId the entity id
	 * @param criterions request criteria
	 * @return the entity or null
	 */
	public LocalizedText getLocalizedText(String id, MedooJunction criterions);

	/**
	 * List entities of type LocalizedText
	 * @param criterion request criteria
	 * @return list of interestType
	 */
	public List<LocalizedText> listLocalizedText(MedooJunction criterion);

	/**
	 * List entities of type LocalizedText
	 * @return list of localizedText
	 */
	public List<LocalizedText> listLocalizedText();

	/**
	 * List entities of type LocalizedText
	 * @param property the property used to sort
	 * @param asc the sort order
	 * @param criterion request criteria
	 * @return list of localizedText
	 */
	public List<LocalizedText> listLocalizedText(String property, boolean asc,
			MedooJunction criterion);

	/**
	 * List entities of type LocalizedText
	 * @param first first index to retrieve
	 * @param max nb of items to retrieve
	 * @param sortProperty the property used to sort the collection
	 * @param sortOrder true for an ascendant sort
	 * @return list of localizedText
	 */
	public List<LocalizedText> listLocalizedText(int first, int max,
			String sortProperty, boolean sortOrder);

	/**
	 * List entities of type LocalizedText
	 * @param first first index to retrieve
	 * @param max nb of items to retrieve	 
	 * @param property the property used to sort the collection
	 * @param sortOrder true for an ascendant sort
	 * @param criterion request criteria
	 * @return list of localizedText
	 */
	public List<LocalizedText> listLocalizedText(int first, int max,
			String property, boolean sortOrder, MedooJunction criterion);

	/**
	 * Count number of LocalizedText in the database
	 * @return the count
	 */
	public int countLocalizedText();

	/**
	 * Count number of LocalizedText in the database, 
	 * that matches the criteria
	 * @return the count
	 */
	public int countLocalizedText(MedooJunction criterion);

	/**
	 * Delete the specified entity
	 * @param entity the entity to delete
	 */
	public void delete(LocalizedText entity);

	/**
	 * Save or update the entity
	 * @param entity the entity to save or update
	 * @param isNew true if it is a new entity added for the first time.
	 */
	public void saveOrUpdate(LocalizedText entity, boolean isNew);

	/**
	 * Save or update the entity without modifing the upload date
	 * @param entity the entity to save or update
	 * @param isNew true if it is a new entity added for the first time.
	 */
	public void saveOrUpdateShadow(LocalizedText entity, boolean isNew);
}
