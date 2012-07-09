package org.imogene.common.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Transactional;

public class GenericDaoImpl implements GenericDao {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@Transactional
	public <T> T load(Class<T> clazz, String id) {
		return em.find(clazz, id);
	}
	
	@Override
	@Transactional
	public <T> List<T> load(Class<T> clazz) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		query.select(query.from(clazz));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public Object load(String className, String id) {
		try {
			return em.find(Class.forName(className), id);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	@Transactional
	public void evict(Object o) {
		em.detach(o);
	}
	
	@Override
	@Transactional
	public <T> void saveOrUpdate(T object) {
		em.<T> merge(object);
	};
	
	@Override
	@Transactional
	public <T> long count(Class<T> clazz) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<T> root = query.from(clazz);
		query.select(builder.count(root));
		return em.createQuery(query).getSingleResult();
	}

}
