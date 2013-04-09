package org.imogene.lib.common.sync.entity;

import java.util.List;

public interface SynchronizableEntityDao {

	public SynchronizableEntity load(String id);

	public List<SynchronizableEntity> load();

	public void store(SynchronizableEntity entity);

	public void delete(SynchronizableEntity entity);

}
