package org.imogene.notifier.server.services;

import java.util.List;
import java.util.Vector;

import org.imogene.common.entity.ImogActor;
import org.imogene.notifier.server.Constants;
import org.imogene.notifier.server.common.NotificationInstance;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


/**
 * Notification by email.
 * @author Medes-IMPS 
 */
public class EmailNotification implements NotificationService {	

	private static String FROM = "contact@imogene.org";

	private JavaMailSender mailSender;

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.notifier.server.services.NotificationService#sendNotification(org.imogene.notifier.server.common.Notification)
	 */
	@Override
	public void sendNotification(NotificationInstance notification) {

		List<SimpleMailMessage> messages = new Vector<SimpleMailMessage>();		
		for (ImogActor actor : notification.getUserRecipients()) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(FROM);
			message.setSubject(notification.getTitle());
			message.setText(notification.getMessage());
			message.setTo(actor.getNotificationData(Constants.MailMethod));
			messages.add(message);
		}		
		mailSender.send(messages.toArray(new SimpleMailMessage[messages.size()]));
	}

	
	/**
	 * Set the mail sender to use
	 * 
	 * @param mailSender
	 *            the mailSender to set
	 */
	public void setMailSender(JavaMailSender pMailSender) {
		mailSender = pMailSender;
	}

}
