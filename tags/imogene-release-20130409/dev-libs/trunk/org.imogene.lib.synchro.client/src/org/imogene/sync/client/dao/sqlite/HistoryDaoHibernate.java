package org.imogene.sync.client.dao.sqlite;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Property;
import org.imogene.sync.client.history.HistoryDao;
import org.imogene.sync.client.history.SyncHistory;


public class HistoryDaoHibernate implements HistoryDao {
		
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.history.HistoryDao#loadLastOk()
	 */
	@SuppressWarnings("unchecked")
	public SyncHistory loadLastOk() {		
		
		Session session = SessionManager.getInstance().getSession();
		List<SyncHistory> result;
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(SyncHistory.class);
			crit.addOrder(Property.forName("date").desc());
			result = crit.list();
			tx.commit();
		}
		if(result != null && result.size()>0) {
			for (SyncHistory history: result) {
				if (history.getStatus()==SyncHistory.STATUS_OK)
					return history;
			}
		}			
		return null;			
	}	
	
	@SuppressWarnings("unchecked")
	public SyncHistory loadLastError() {
		Session session = SessionManager.getInstance().getSession();
		List<SyncHistory> result;
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			Criteria crit = session.createCriteria(SyncHistory.class);
			crit.addOrder(Property.forName("date").desc());
			result = crit.list();
			tx.commit();
		}
		if(result != null && result.size()>0) {
			for (SyncHistory history: result) {
				if (history.getStatus()==SyncHistory.STATUS_ERROR)
					return history;
			}
		}			
		return null;		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.history.HistoryDao#store(org.imogene.sync.history.SyncHistory)
	 */
	public void store(SyncHistory history) {
		Session session = SessionManager.getInstance().getSession();
		synchronized (session) {
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(history);
			tx.commit();
		}
	}	
	
	@SuppressWarnings("unchecked")
	public void deleteOldHistories() {
		
		Session session = SessionManager.getInstance().getSession();
		List<SyncHistory> result;
		synchronized (session) {
			Transaction tx1 = session.beginTransaction();
			Criteria crit = session.createCriteria(SyncHistory.class);
			crit.addOrder(Property.forName("date").desc());
			result = crit.list();
			tx1.commit();
			
			boolean toDelete = false;
			boolean isStatusOK = false;
			int i = 1;
			if(result != null && result.size()>0) {
				for (SyncHistory history: result) {
					
					if (toDelete && isStatusOK) {
						Transaction tx2 = session.beginTransaction();
						session.delete(history);
						tx2.commit();
					}
					
					if (!toDelete && i>=3)
						toDelete = true;
					if (!isStatusOK && history.getStatus()==SyncHistory.STATUS_OK)
						isStatusOK = true;

					i = i + 1;
				}
			}			
			
		}		

	}
}
