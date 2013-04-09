package org.imogene.sync.server.clientfilter;

import java.util.Date;
import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.uao.clientfilter.ClientFilter;

/**
 * Implements a Hibernate DAO for the ClientFilter 
 * @author MEDES-IMPS
 */
public interface ClientFilterDao extends EntityDao {
	
	public List<ClientFilter> loadFilters(String userId, String terminalId, String classname);
	
	public List<ClientFilter> loadFilters(String userId, String terminalId);

	public List<Synchronizable> loadEntities();

	public List<Synchronizable> loadEntities(ImogJunction criterions);

	public Synchronizable loadEntity(String entityId);

	public List<Synchronizable> loadModified(Date date);

	public List<Synchronizable> loadModified(Date date, ImogJunction criterions);

	public Synchronizable loadModified(Date date, String entityId);

	public Synchronizable loadModified(Date date, ImogJunction criterions,	String entityId);

	public void saveOrUpdate(Synchronizable entity);

	public void merge(Synchronizable entity);

	public int countAll();

	public void delete(Synchronizable entity);

	public void deleteEntities();

	public void clear();

	public void flush();

}
