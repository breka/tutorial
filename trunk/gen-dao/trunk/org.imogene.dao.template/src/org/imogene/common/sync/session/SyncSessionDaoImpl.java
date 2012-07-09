package org.imogene.common.sync.session;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Transactional;

/**
 * sync session DAO implementation using Hibernate.
 * 
 * @author Medes-IMPS
 */
public class SyncSessionDaoImpl implements SyncSessionDao {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@Transactional
	public void saveOrUpdate(SyncSession session) {
		em.<SyncSession> merge(session);
	}

	@Override
	@Transactional
	public SyncSession load(String id) {
		return em.find(SyncSession.class, id);
	}

	@Override
	@Transactional
	public void delete(SyncSession session) {
		em.remove(session);
	}

	@Override
	@Transactional
	public void clearTerminated() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<SyncSession> query = builder.createQuery(SyncSession.class);
		Root<SyncSession> root = query.from(SyncSession.class);
		query.select(root);
		query.where(root.<Date> get("sendDate").isNull());
		List<SyncSession> sessions = em.createQuery(query).getResultList();
		for (SyncSession session : sessions) {
			delete(session);
		}
	}

	@Override
	@Transactional
	public boolean isValid(String id) {
		if (load(id) != null) {
			return true;
		}
		return false;
	}

}