package org.imogene.rcp.core.view;

public interface SyncView {

	public void addMessage(String message, int type);
	
	public void updateDate();
	
	public void clearMessage();
}
