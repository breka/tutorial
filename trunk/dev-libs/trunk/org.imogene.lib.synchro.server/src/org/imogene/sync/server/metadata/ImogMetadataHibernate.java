package org.imogene.sync.server.metadata;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ImogMetadataHibernate extends HibernateDaoSupport implements ImogMetadataDao {

	public ImogMetadata get(Date date, String terminalId) {		
		return null;
	}
	
	public List<ImogMetadata> list() {		
		return getHibernateTemplate().loadAll(ImogMetadata.class);
	}

	public void saveOrUpdate(ImogMetadata data) {		
		getHibernateTemplate().save(data);
	}
	
}
