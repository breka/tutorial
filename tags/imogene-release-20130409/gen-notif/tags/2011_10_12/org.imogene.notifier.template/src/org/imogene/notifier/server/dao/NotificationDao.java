package org.imogene.notifier.server.dao;

import java.util.List;

import org.imogene.notifier.server.common.NotificationTemplate;


public interface NotificationDao {

	public List<NotificationTemplate> getNotification(String type, String ope);
}
