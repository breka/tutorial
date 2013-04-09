package org.imogene.sync.server.policy;

import java.util.Date;

public interface SyncServerPolicy {

	public Date computeDate(String terminalId);
}
