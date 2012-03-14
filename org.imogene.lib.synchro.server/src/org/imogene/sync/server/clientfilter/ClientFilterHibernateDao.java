package org.imogene.sync.server.clientfilter;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.imogene.common.dao.criteria.HibernateDaoUtil;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.uao.clientfilter.ClientFilter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implements a Hibernate DAO for the ClientFilter 
 * @author MEDES-IMPS
 */
public class ClientFilterHibernateDao extends HibernateDaoSupport implements ClientFilterDao {

	private Logger logger = Logger.getLogger("org.imogene.clientfilter.dao");
	
	
	@SuppressWarnings("unchecked")
	public List<ClientFilter> loadFilters(String userId, String terminalId, String classname) {
		Criteria crit = getSession().createCriteria(ClientFilter.class);
		crit.add(Restrictions.eq("userId", userId));
		crit.add(Restrictions.eq("terminalId", terminalId));
		crit.add(Restrictions.eq("cardEntity", classname));
		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ClientFilter> loadFilters(String userId, String terminalId) {
		Criteria crit = getSession().createCriteria(ClientFilter.class);
		crit.add(Restrictions.eq("userId", userId));
		crit.add(Restrictions.eq("terminalId", terminalId));
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Synchronizable> loadEntities() {
		logger.debug("Loading all ClientFilters");
		Criteria crit = getSession().createCriteria(ClientFilter.class);
		return crit.list();
	}

	public List<Synchronizable> loadEntities(int arg0, int arg1, String arg2, boolean arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Synchronizable> loadEntities(ImogJunction criterions) {
		logger.debug("Loading all ClientFilters");
		Criteria crit = getSession().createCriteria(ClientFilter.class);
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public Synchronizable loadEntity(String entityId) {
		logger.debug("Loading the ClientFilter with id '" + entityId + "'.");
		Criteria crit = getSession().createCriteria(ClientFilter.class);
		crit.add(Restrictions.eq("id", entityId));
		List resultList = crit.list();
		if (resultList != null && resultList.size() == 1)
			return (ClientFilter) resultList.get(0);

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Synchronizable> loadModified(Date date) {
		logger.debug("Loading ClientFilters modified since '" + date.toString()
				+ "'.");
		Criteria crit = getSession().createCriteria(ClientFilter.class);
		crit.add(Restrictions.ge("uploadDate", date));
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Synchronizable> loadModified(Date date, ImogJunction criterions) {
		logger.debug("Loading ClientFilters modified since '" + date.toString()
				+ "'.");
		Criteria crit = getSession().createCriteria(ClientFilter.class);
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		crit.add(Restrictions.ge("uploadDate", date));
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public Synchronizable loadModified(Date date, String entityId) {
		logger.debug("Loading ClientFilter with id='" + entityId
				+ "' if modified since '" + date.toString() + "'.");
		Criteria crit = getSession().createCriteria(ClientFilter.class);
		crit.add(Restrictions.eq("id", entityId));
		crit.add(Restrictions.ge("uploadDate", date));

		List resultList = crit.list();
		if (resultList != null && resultList.size() == 1)
			return (ClientFilter) resultList.get(0);

		return null;
	}

	@SuppressWarnings("unchecked")
	public Synchronizable loadModified(Date date, ImogJunction criterions,
			String entityId) {
		logger.debug("Loading the ClientFilter with id '" + entityId
				+ "' modified since '" + date + "'.");
		Criteria crit = getSession().createCriteria(ClientFilter.class);
		crit.add(HibernateDaoUtil.addImogJunction(criterions));
		crit.add(Restrictions.ge("uploadDate", date));
		crit.add(Restrictions.eq("id", entityId));

		List resultList = crit.list();
		if (resultList != null && resultList.size() == 1)
			return (ClientFilter) resultList.get(0);

		return null;
	}

	public void saveOrUpdate(Synchronizable entity) {
		logger.debug("Saving or updating ClientFilter with id '"
				+ entity.getId() + "'.");
		getSession().beginTransaction();
		getHibernateTemplate().saveOrUpdate(entity);
		getSession().getTransaction().commit();
	}

	public void merge(Synchronizable entity) {
		logger.debug("Merging ClientFilter with id '" + entity.getId() + "'.");
		getSession().beginTransaction();
		getHibernateTemplate().merge(entity);
		getSession().getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public int countAll() {
		Criteria crit = getSession().createCriteria(ClientFilter.class);
		crit.setProjection(Projections.rowCount());
		List list = crit.list();
		return Integer.parseInt(list.get(0).toString());
	}

	public void delete(Synchronizable entity) {
		logger.debug("Deleting ClientFilter with id '" + entity.getId() + "'.");
		getHibernateTemplate().delete(entity);
	}

	public void deleteEntities() {
		logger.debug("Deleting ClientFilters");
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
