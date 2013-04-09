package org.imogene.lib.common.sync.parameter;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.transaction.annotation.Transactional;

public class SyncParameterDaoImpl implements SyncParameterDao {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@Transactional
	public SyncParameter load() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<SyncParameter> query = builder.createQuery(SyncParameter.class);
		query.select(query.from(SyncParameter.class));
		try {
			return em.createQuery(query).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional
	public void store(SyncParameter parameters) {
		em.<SyncParameter> merge(parameters);
	}

}
