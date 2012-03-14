package org.imogene.sync.server.localizedtext;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.imogene.common.dao.LocalizedTextDao;
import org.imogene.common.dao.criteria.HibernateDaoUtil;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.sync.localizedtext.LocalizedText;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implements a Hibernate DAO for the LocalizedText 
 * @author Medes-IMPS
 */
public class LocalizedTextHibernateDao extends HibernateDaoSupport
		implements
			LocalizedTextDao {

	private Logger logger = Logger.getLogger("org.imogene.translatable.dao");
	
	public List<LocalizedText> listLocalizedText(String fieldId) {
		logger.debug("Loading LocalizedTexts for fieldId= " + fieldId);
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.add(Restrictions.eq("fieldId", fieldId));
		return crit.list();
	}

	public List<Synchronizable> loadEntities() {
		logger.debug("Loading all LocalizedTexts");
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		return crit.list();
	}

	public List<Synchronizable> loadEntities(int arg0, int arg1, String arg2,
			boolean arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Synchronizable> loadEntities(ImogJunction criterions) {
		logger.debug("Loading all LocalizedTexts");
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		return crit.list();
	}

	public Synchronizable loadEntity(String entityId) {
		logger.debug("Loading the LocalizedText with id '" + entityId + "'.");
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.add(Restrictions.eq("id", entityId));
		List resultList = crit.list();
		if (resultList != null && resultList.size() == 1)
			return (LocalizedText) resultList.get(0);

		return null;
	}

	public List<Synchronizable> loadModified(Date date) {
		logger.debug("Loading LocalizedTexts modified since '"
				+ date.toString() + "'.");
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.add(Restrictions.ge("uploadDate", date));
		return crit.list();
	}

	public List<Synchronizable> loadModified(Date date, ImogJunction criterions) {
		logger.debug("Loading LocalizedTexts modified since '"
				+ date.toString() + "'.");
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		crit.add(Restrictions.ge("uploadDate", date));
		return crit.list();
	}

	public Synchronizable loadModified(Date date, String entityId) {
		logger.debug("Loading LocalizedText with id='" + entityId
				+ "' if modified since '" + date.toString() + "'.");
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.add(Restrictions.eq("id", entityId));
		crit.add(Restrictions.ge("uploadDate", date));

		List resultList = crit.list();
		if (resultList != null && resultList.size() == 1)
			return (LocalizedText) resultList.get(0);

		return null;
	}

	public Synchronizable loadModified(Date date, ImogJunction criterions,
			String entityId) {
		logger.debug("Loading the LocalizedText with id '" + entityId
				+ "' modified since '" + date + "'.");
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		crit.add(Restrictions.ge("uploadDate", date));
		crit.add(Restrictions.eq("id", entityId));

		List resultList = crit.list();
		if (resultList != null && resultList.size() == 1)
			return (LocalizedText) resultList.get(0);

		return null;
	}

	public void saveOrUpdate(Synchronizable entity) {
		logger.debug("Saving or updating LocalizedText with id '"
				+ entity.getId() + "'.");
		getSession().beginTransaction();
		getHibernateTemplate().saveOrUpdate(entity);
		getSession().getTransaction().commit();
	}

	public void merge(Synchronizable entity) {
		logger.debug("Merging LocalizedText with id '" + entity.getId() + "'.");
		getSession().beginTransaction();
		getHibernateTemplate().merge(entity);
		getSession().getTransaction().commit();
	}

	public int countAll() {
		Criteria crit = getSession().createCriteria(LocalizedText.class);
		crit.setProjection(Projections.rowCount());
		List list = crit.list();
		return Integer.parseInt(list.get(0).toString());
	}

	public void delete(Synchronizable entity) {
		logger.debug("Deleting LocalizedText with id '" + entity.getId() + "'.");
		getHibernateTemplate().delete(entity);
	}

	public void deleteEntities() {
		logger.debug("Deleting LocalizedTexts");
		String hsql = "delete from Doctor";
		Query query = getSession().createQuery(hsql);
		query.executeUpdate();
	}

	public void clear() {
		getHibernateTemplate().clear();
	}

	public void flush() {
		getHibernateTemplate().flush();
	}

}
