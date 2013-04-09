package org.imogene.sync.client.history;

public interface HistoryDao {

	public void store(SyncHistory history);
	
	public SyncHistory loadLastOk();	
	
	public SyncHistory loadLastError();
	
	public void deleteOldHistories();
}
