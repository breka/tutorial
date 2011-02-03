package org.imogene.sync.server.metadata;

import java.util.Date;
import java.util.List;

public interface ImogMetadataDao {

	
	public void saveOrUpdate(ImogMetadata data);
	
	public ImogMetadata get(Date date, String terminalId);
	
	public List<ImogMetadata> list();
}
