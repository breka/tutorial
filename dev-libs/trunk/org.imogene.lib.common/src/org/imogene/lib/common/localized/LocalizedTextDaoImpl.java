package org.imogene.lib.common.localized;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.imogene.lib.common.dao.ImogBeanDaoImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements a Hibernate DAO for the LocalizedText
 * 
 * @author Medes-IMPS
 */
public class LocalizedTextDaoImpl extends ImogBeanDaoImpl<LocalizedText> implements LocalizedTextDao {

	protected LocalizedTextDaoImpl() {
		super(LocalizedText.class);
	}

	@Override
	@Transactional
	public List<LocalizedText> listForField(String fieldId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<LocalizedText> query = builder.createQuery(LocalizedText.class);
		Root<LocalizedText> root = query.from(LocalizedText.class);
		query.select(root);
		query.where(builder.equal(root.<String> get("fieldId"), fieldId));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public void delete() {
		em.createQuery("DELETE FROM LocalizedText").executeUpdate();
	}

}
