package org.imogene.lib.common.localized;

import java.util.List;

import org.imogene.lib.common.dao.ImogBeanDao;

/**
 * Manage persistence for LocalizedText
 * 
 * @author MEDES-IMPS
 */
public interface LocalizedTextDao extends ImogBeanDao<LocalizedText> {

	/**
	 * Load the entities of type LocalizedText for the field fieldId
	 * 
	 * @param fieldId the id of the field for which localized texts should be
	 *            gotten
	 * @return a list of localizedText
	 */
	public List<LocalizedText> listForField(String fieldId);
}
