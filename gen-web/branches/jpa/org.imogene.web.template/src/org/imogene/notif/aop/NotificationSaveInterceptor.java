package org.imogene.notif.aop;

import java.lang.reflect.Method;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.imogene.common.entity.ImogBean;
import org.imogene.common.entity.ShortNameHelper;
import org.springframework.aop.AfterReturningAdvice;

public class NotificationSaveInterceptor implements AfterReturningAdvice {

	private String notifierHost = "http://localhost:8080/DiabsatNotif";

	private Logger logger = Logger.getLogger("org.imogene.notif.aop");

	private ShortNameHelper shortNameHelper;

	/**
	 * Set the short name helper
	 * 
	 * @param nameHelper
	 *            the short name helper
	 */
	public void setShortNameHelper(ShortNameHelper nameHelper) {
		shortNameHelper = nameHelper;
	}

	/**
	 * Set the URL of the notification server
	 * 
	 * @param notifier
	 *            the notification server URL
	 */
	public void setNotifierUrl(String notifier) {
		notifierHost = notifier;
	}

	/**
	 * Notify about the entity modification/creation
	 */
	public void afterReturning(Object result, Method method, Object[] args,
			Object target) throws Throwable {

		if (method.getName().startsWith("save"))
			handleSaveOrUpdate(args, target);
	}

	/**
	 * Handle the saveOrUpdate method
	 * 
	 * @param args
	 *            arguments of the method
	 * @param target
	 *            the entity target
	 */
	private void handleSaveOrUpdate(Object[] args, Object target) {

		/* handle the entity */
		ImogBean bean = (ImogBean) args[0];
		Boolean isNew = (Boolean) args[1];
		final String id = bean.getId();
		final String operation = getOperation(isNew);
		final String type = shortNameHelper.getShortName(bean.getClass()
				.getName());

		/* thread the notification process */
		new Thread() {
			@Override
			public void run() {
				sendToNotifier(type, operation, id);
			}
		}.start();
	}

	private String getOperation(Boolean isNew) {
		if (isNew)
			return "create";
		else
			return "modify";
	}

	/**
	 * Notify the notifier by http.
	 * 
	 * @param type
	 *            the card type
	 * @param operation
	 *            the operation on the card
	 * @param id
	 *            the card id
	 */
	private void sendToNotifier(String type, String operation, String id) {
		HttpClient client = new HttpClient();
		String uri = notifierHost + "?" + "type=" + type + "&op=" + operation
				+ "&id=" + id;
		logger.debug("Notifier URI : " + uri);
		GetMethod method = new GetMethod(uri);
		try {
			client.executeMethod(method);
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
		}
	}

}
