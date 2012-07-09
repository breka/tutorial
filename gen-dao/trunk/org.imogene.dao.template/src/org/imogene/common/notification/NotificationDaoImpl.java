package org.imogene.common.notification;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.imogene.common.criteria.DaoUtil;
import org.imogene.common.criteria.ImogCriterion;
import org.springframework.transaction.annotation.Transactional;

public class NotificationDaoImpl implements NotificationDao {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@Transactional
	public Notification load(String id) {
		return em.find(Notification.class, id);
	}

	@Override
	@Transactional
	public List<Notification> load(String sourceCard, String operation) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Notification> query = builder.createQuery(Notification.class);
		Root<Notification> root = query.from(Notification.class);
		query.select(root);
		query.where(builder.like(root.<String> get("operation"), operation),
				builder.like(root.<String> get("sourceCard"), sourceCard));
		return em.createQuery(query).getResultList();
	}

	@Override
	@Transactional
	public List<Notification> load(int first, int max, String property, boolean asc, ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Notification> query = builder.createQuery(Notification.class);
		Root<Notification> root = query.from(Notification.class);
		query.select(root);
		query.where(DaoUtil.<Notification> toPredicate(criterion, builder, root));
		if (property == null) {
			property = "name";
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
	public long count() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Notification> root = query.from(Notification.class);
		query.select(builder.count(root));
		return em.createQuery(query).getSingleResult();
	}

	@Override
	@Transactional
	public long count(ImogCriterion criterion) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Notification> root = query.from(Notification.class);
		query.select(builder.count(root));
		query.where(DaoUtil.<Notification> toPredicate(criterion, builder, root));
		return em.createQuery(query).getSingleResult();
	}

	@Override
	@Transactional
	public void saveOrUpdate(Notification entity) {
		em.<Notification> merge(entity);
	}

}
