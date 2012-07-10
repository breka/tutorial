package org.imogene.lib.common.metadata;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Transactional;

public class ImogMetadataDaoImpl implements ImogMetadataDao {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@Transactional
	public ImogMetadata load(Date date, String terminalId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ImogMetadata> query = builder.createQuery(ImogMetadata.class);
		Root<ImogMetadata> root = query.from(ImogMetadata.class);
		query.select(root);
		query.where(builder.<Date> greaterThanOrEqualTo(root.<Date> get("date"), date),
				builder.equal(root.<String> get("terminalId"), terminalId));
		try {
			return em.createQuery(query).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional
	public List<ImogMetadata> load() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ImogMetadata> query = builder.createQuery(ImogMetadata.class);
		query.select(query.from(ImogMetadata.class));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public void saveOrUpdate(ImogMetadata data) {
		em.<ImogMetadata> merge(data);
	}

}
