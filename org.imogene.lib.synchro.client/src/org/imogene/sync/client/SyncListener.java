package org.imogene.sync.client;


public interface SyncListener {

	public void initSession(String id);
	
	public void syncError(int code);
	
	public void sending(int bytesToSend);
	
	public void resumeSend(int bytesToSend, int allBytes);
	
	public void receiving(int bytesToReceive);
	
	public void resumeReceive(int bytesToReceive, int allBytes);
			
	public void finish();
}
