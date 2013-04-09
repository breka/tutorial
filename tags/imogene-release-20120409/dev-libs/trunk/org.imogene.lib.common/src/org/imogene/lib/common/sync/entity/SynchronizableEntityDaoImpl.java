package org.imogene.lib.common.sync.entity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.transaction.annotation.Transactional;

public class SynchronizableEntityDaoImpl implements SynchronizableEntityDao {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@Transactional
	public SynchronizableEntity load(String id) {
		return em.find(SynchronizableEntity.class, id);
	}

	@Override
	@Transactional
	public List<SynchronizableEntity> load() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<SynchronizableEntity> query = builder.createQuery(SynchronizableEntity.class);
		query.select(query.from(SynchronizableEntity.class));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public void store(SynchronizableEntity entity) {
		em.<SynchronizableEntity> merge(entity);
	}

	@Override
	@Transactional
	public void delete(SynchronizableEntity entity) {
		em.remove(entity);

	}

}
