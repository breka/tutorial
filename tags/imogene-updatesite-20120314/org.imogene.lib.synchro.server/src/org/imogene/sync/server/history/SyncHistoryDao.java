package org.imogene.sync.server.history;

import java.util.List;

public interface SyncHistoryDao {

	/**
	 * Save a synchronization history item
	 * @param history the history item to save
	 */
	public void saveOrUpdate(SyncHistory history);
	
	/**
	 * Load the last successful history 
	 * record for the specified terminal.
	 * @param terminalId the terminal id
	 * @return the last successful history
	 */
	public SyncHistory loadLastHistory(String terminalId);
	
	/**
	 * Load all history records for a given terminal
	 * @param terminalId the terminal id
	 * @return the list of histories
	 */
	public List<SyncHistory> loadHistories(String terminalId);
	
	
	/**
	 * Load all history records
	 * @return the list of histories
	 */
	public List<SyncHistory> loadHistories();	
	
	
	/**
	 * Delete old Histories
	 * @param terminalId the terminal id
	 */
	public void deleteOldHistories(String terminalId);
	
	/**
	 * For tests: Delete all stored histories
	 */
	public void deleteHistories();
	
	
}
