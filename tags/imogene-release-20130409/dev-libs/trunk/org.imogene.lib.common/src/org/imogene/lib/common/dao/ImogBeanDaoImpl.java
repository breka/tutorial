package org.imogene.lib.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.imogene.lib.common.criteria.DaoUtil;
import org.imogene.lib.common.criteria.ImogCriterion;
import org.imogene.lib.common.entity.ImogBean;
import org.springframework.transaction.annotation.Transactional;

public abstract class ImogBeanDaoImpl<T extends ImogBean> implements ImogBeanDao<T> {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager em;

	protected final Class<T> clazz;

	protected ImogBeanDaoImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	@Transactional
	public T load(String id) {
		return em.find(clazz, id);
	}

	@Override
	@Transactional
	public List<T> load(List<String> ids) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(root.<String> get("id").in(ids));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public T load(String id, ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.equal(root.<String> get("id"), id), DaoUtil.<T> toPredicate(criterion, builder, root));
		try {
			return em.createQuery(query).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional
	public List<T> load(ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(DaoUtil.<T> toPredicate(criterion, builder, root));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public List<T> load(String property, boolean asc, ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(DaoUtil.<T> toPredicate(criterion, builder, root));
		if (property == null) {
			property = "modified";
		}
		if (asc) {
			query.orderBy(builder.asc(root.get(property)));
		} else {
			query.orderBy(builder.desc(root.get(property)));
		}
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public List<T> load(int first, int max, String property, boolean asc) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		if (property == null) {
			property = "modified";
		}
		if (asc) {
			query.orderBy(builder.asc(root.get(property)));
		} else {
			query.orderBy(builder.desc(root.get(property)));
		}
		return em.createQuery(query).setFirstResult(first).setMaxResults(max).getResultList();
	}

	@Override
	@Transactional
	public List<T> load(int first, int max, String property, boolean asc, ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(DaoUtil.<T> toPredicate(criterion, builder, root));
		if (property == null) {
			property = "modified";
		}
		if (asc) {
			query.orderBy(builder.asc(root.get(property)));
		} else {
			query.orderBy(builder.desc(root.get(property)));
		}
		query.orderBy(builder.desc(root.<String> get("id")));
		return em.createQuery(query).setFirstResult(first).setMaxResults(max).getResultList();
	}

	@Override
	@Transactional
	public long count() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<T> root = query.from(clazz);
		query.select(builder.count(root));
		return em.createQuery(query).getSingleResult();
	}

	@Override
	@Transactional
	public long count(ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<T> root = query.from(clazz);
		query.select(builder.count(root));
		query.where(DaoUtil.<T> toPredicate(criterion, builder, root));
		return em.createQuery(query).getSingleResult();
	}

	@Override
	@Transactional
	public void delete(T entity) {
		em.remove(entity);
	}

	@Override
	@Transactional
	public List<T> loadNonAffected(String property, ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(root.get(property).isNull(), DaoUtil.<T> toPredicate(criterion, builder, root));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public List<T> loadNonAffected(int first, int max, String sortProperty, boolean asc, String property,
			ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(root.get(property).isNull(), DaoUtil.<T> toPredicate(criterion, builder, root));
		if (sortProperty == null) {
			sortProperty = "modified";
		}
		if (asc) {
			query.orderBy(builder.asc(root.get(sortProperty)));
		} else {
			query.orderBy(builder.desc(root.get(sortProperty)));
		}
		return em.createQuery(query).setFirstResult(first).setMaxResults(max).getResultList();
	}

	@Override
	@Transactional
	public List<T> loadNonAffectedReverse(int first, int max, String sortProperty, boolean asc, String property,
			ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(root.<T, Object> join(property, JoinType.LEFT).<String> get("id").isNull(),
				DaoUtil.<T> toPredicate(criterion, builder, root));
		if (sortProperty == null) {
			sortProperty = "modified";
		}
		if (asc) {
			query.orderBy(builder.asc(root.get(sortProperty)));
		} else {
			query.orderBy(builder.desc(root.get(sortProperty)));
		}
		return em.createQuery(query).setFirstResult(first).setMaxResults(max).getResultList();
	}

	@Override
	@Transactional
	public List<T> loadAffectedCardNProperty(String property, String id) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.equal(root.<T, Object> join(property, JoinType.INNER).<String> get("id"), id));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public long countNonAffected(String property, ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<T> root = query.from(clazz);
		query.select(builder.count(root));
		query.where(root.get(property).isNull(), DaoUtil.<T> toPredicate(criterion, builder, root));
		return em.createQuery(query).getSingleResult();
	}

	@Override
	@Transactional
	public long countNonAffectedReverse(String property, ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<T> root = query.from(clazz);
		query.select(builder.count(root));
		query.where(root.join(property, JoinType.LEFT).<String> get("id").isNull(),
				DaoUtil.<T> toPredicate(criterion, builder, root));
		return em.createQuery(query).getSingleResult();
	}

	@Override
	@Transactional
	public void saveOrUpdate(T entity) {
		entity.setUploadDate(new Date(System.currentTimeMillis()));
		em.<T> merge(entity);
	}
	
	@Override
	@Transactional
	public void saveOrUpdate(T entity, boolean isNew) {
		saveOrUpdate(entity);
	};

	@Override
	@Transactional
	public void saveOrUpdateShadow(T entity) {
		em.<T> merge(entity);
	}
	
	@Override
	@Transactional
	public void saveOrUpdateShadow(T entity, boolean isNew) {
		saveOrUpdateShadow(entity);
	};

	@Override
	@Transactional
	public List<T> load() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		query.select(query.from(clazz));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public List<T> loadModified(Date date) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.<Date> greaterThanOrEqualTo(root.<Date> get("modified"), date));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public List<T> loadModified(Date date, ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.<Date> greaterThanOrEqualTo(root.<Date> get("modified"), date),
				DaoUtil.toPredicate(criterion, builder, root));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public T loadModified(Date date, String id) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.<Date> greaterThanOrEqualTo(root.<Date> get("modified"), date),
				builder.equal(root.<String> get("id"), id));
		try {
			return em.createQuery(query).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional
	public T loadModified(Date date, ImogCriterion criterion, String id) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.<Date> greaterThanOrEqualTo(root.<Date> get("modified"), date),
				builder.equal(root.<String> get("id"), id), DaoUtil.toPredicate(criterion, builder, root));
		try {
			return em.createQuery(query).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional
	public List<T> loadUploaded(Date date) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.<Date> greaterThanOrEqualTo(root.<Date> get("uploadDate"), date));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public List<T> loadUploaded(Date date, ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.<Date> greaterThanOrEqualTo(root.<Date> get("uploadDate"), date),
				DaoUtil.toPredicate(criterion, builder, root));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public T loadUploaded(Date date, String id) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.<Date> greaterThanOrEqualTo(root.<Date> get("uploadDate"), date),
				builder.equal(root.<String> get("id"), id));
		try {
			return em.createQuery(query).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional
	public T loadUploaded(Date date, ImogCriterion criterion, String id) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.<Date> greaterThanOrEqualTo(root.<Date> get("uploadDate"), date),
				builder.equal(root.<String> get("id"), id), DaoUtil.toPredicate(criterion, builder, root));
		try {
			return em.createQuery(query).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional
	public void merge(T entity) {
		em.<T> merge(entity);
	}

	@Override
	@Transactional
	public void clear() {
		em.clear();
	}

	@Override
	@Transactional
	public void flush() {
		em.flush();
	}

}
