package org.imogene.lib.common.metadata;

import java.util.Date;
import java.util.List;

public interface ImogMetadataDao {

	public void saveOrUpdate(ImogMetadata data);

	public ImogMetadata load(Date date, String terminalId);

	public List<ImogMetadata> load();
}
