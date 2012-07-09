package org.imogene.web.server.dao.hibernate;

import java.util.List;

import org.imogene.web.gwt.common.criteria.ImogJunction;
import org.imogene.web.gwt.common.entity.LocalizedText;

/**
 * Manage persistence for LocalizedText
 * @author MEDES-IMPS
 */
public interface LocalizedTextDao {

	/**
	 * Load the entity with the specified id
	 * @param entityId the entity id
	 * @return the entity or null
	 */
	public LocalizedText getLocalizedText(String id);
	
	/**
	 * Load the entities of type LocalizedText for the field fieldId
	 * @param fieldId the id of the field for which localized texts should be gotten
	 * @return a list of localizedText
	 */
	public List<LocalizedText> listLocalizedText(String fieldId);

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
	public LocalizedText getLocalizedText(String id, ImogJunction criterions);

	/**
	 * List entities of type LocalizedText
	 * @param criterion request criteria
	 * @return list of localizedText
	 */
	public List<LocalizedText> listLocalizedText(ImogJunction criterion);

	/**
	 * List entities of type LocalizedText
	 * @param property the property used to sort
	 * @param asc the sort order
	 * @param criterion request criteria
	 * @return list of localizedText
	 */
	public List<LocalizedText> listLocalizedText(String property, boolean asc,
			ImogJunction criterion);

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
			String property, boolean sortOrder, ImogJunction criterion);

	/**
	 * Count number of localizedText in the database
	 * @return the count
	 */
	public int countLocalizedText();

	/**
	 * Count number of localizedText in the database, 
	 * that matches the criteria
	 * @return the count
	 */
	public int countLocalizedText(ImogJunction criterion);

	/**
	 * Delete the specified entity
	 * @param entity the entity to delete
	 */
	public void delete(LocalizedText entity);

	/**
	 * List all the non affected LocalizedText
	 * @param property the property used by the relation
	 * @param criterion the request criteria
	 * @return a list of LocalizedText
	 */
	public List<LocalizedText> listNonAffectedLocalizedText(String property,
			ImogJunction criterions);

	/**
	 * List the non affected LocalizedText
	 * @param first the first entities to retrieve
	 * @param max the number of entities to retrieve
	 * @param sortProperty the property used to sort
	 * @param asc the sort order
	 * @param property the property used by the relation
	 * @param criterion the request criteria
	 * @return a list of LocalizedText
	 */
	public List<LocalizedText> listNonAffectedLocalizedText(int first, int max,
			String sortProperty, boolean asc, String property,
			ImogJunction criterions);

	/**
	 * List the non affected LocalizedText for th reverse relations
	 * @param first the first entities to retrieve
	 * @param max the number of entities to retrieve
	 * @param sortProperty the property used to sort
	 * @param asc the sort order
	 * @param property the property used by the relation
	 * @param criterion the request criteria
	 * @return a list of LocalizedText
	 */
	public List<LocalizedText> listNonAffectedLocalizedTextReverse(int first,
			int max, String sortProperty, boolean asc, String property,
			ImogJunction criterions);

	/**
	 * List ???
	 * @param property the property used for the relation
	 * @param id the entity id
	 * @return a list of LocalizedText
	 */
	public List<LocalizedText> listAffectedCardNProperty(String property,
			String id);

	/**
	 * Count the non affected LocalizedText
	 * @param property the property used by the relation
	 * @param criterions the request criterions
	 * @return the count
	 */
	public int countNonAffectedLocalizedText(String property,
			ImogJunction criterions);

	/**
	 * Count the non affected LocalizedText reverse relation
	 * @param property the property used by the relation
	 * @param criterions the request criterions
	 * @return the count
	 */
	public int countNonAffectedLocalizedTextReverse(String property,
			ImogJunction criterions);

	/**
	 * Save or update the entity
	 * @param entity the entity to save or update
	 */
	public void saveOrUpdate(LocalizedText entity);

	/**
	 * Save or update the entity without modifing the upload date
	 * @param entity the entity to save or update
	 */
	public void saveOrUpdateShadow(LocalizedText entity);

	/* relation dependences */

}
