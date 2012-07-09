package org.imogene.common.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.imogene.common.entity.ImogActor;
import org.springframework.transaction.annotation.Transactional;

public abstract class ImogActorDaoImpl<T extends ImogActor> extends ImogBeanDaoImpl<T> implements ImogActorDao<T> {

	protected ImogActorDaoImpl(Class<T> clazz) {
		super(clazz);
	}

	@Override
	@Transactional
	public List<T> loadFromLogin(String login) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		query.where(builder.equal(root.<String> get("login"), login));
		return em.createQuery(query).getResultList();
	}

}
