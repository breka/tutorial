package org.imogene.web.gwt.client.ui;

import java.util.HashSet;
import java.util.Set;

public class MessageManager {

	public static final int INFO_TYPE = 0;
	public static final int WARNING_TYPE = 1;
	public static final int ERROR_TYPE = 2;
	public static final int NO_DELAY = -1;

	private static MessageManager instance;
	private Set<MessageListener> listeners = new HashSet<MessageListener>();
	private int messageCounter = 0;

	/**
	 * @return the unique message manager
	 */
	public static MessageManager get() {
		if (instance == null)
			instance = new MessageManager();
		return instance;
	}

	/**
	 * Add a message listener
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addMessageListener(MessageListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove the message listener
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeMessageListener(MessageListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Ass a new info message.
	 * 
	 * @param message
	 *            the message text
	 * @return the message id in this session
	 */
	public int newInfoMessage(String message) {
		return newMessage(message, INFO_TYPE, NO_DELAY);
	}

	/**
	 * Ass a new info message.
	 * 
	 * @param message
	 *            the message text
	 * @param life
	 *            the message life delay (in s);
	 * @return the message id in this session
	 */
	public int newInfoMessage(String message, int life) {
		return newMessage(message, INFO_TYPE, life);
	}

	/**
	 * Ass a new warning message.
	 * 
	 * @param message
	 *            the message text
	 * @param life
	 *            the message life (in s)
	 * @return the message id in this session
	 */
	public int newWarningMessage(String message, int life) {
		return newMessage(message, WARNING_TYPE, life);
	}

	/**
	 * Ass a new warning message.
	 * 
	 * @param message
	 *            the message text
	 * @return the message id in this session
	 */
	public int newWarningMessage(String message) {
		return newMessage(message, WARNING_TYPE, NO_DELAY);
	}

	/**
	 * Add a new error message.
	 * 
	 * @param message
	 *            the message text
	 * @return the message id in this session
	 */
	public int newErrorMessage(String message) {
		return newMessage(message, ERROR_TYPE, NO_DELAY);
	}

	/**
	 * Add a new error message.
	 * 
	 * @param message
	 *            the message text
	 * @param delay
	 *            the message life delay
	 * @return the message id in this session
	 */
	public int newErrorMessage(String message, int delay) {
		return newMessage(message, ERROR_TYPE, delay);
	}

	/**
	 * Add a new message
	 * 
	 * @param message
	 *            the new incoming message
	 * @param type
	 *            the message type
	 * @return the message id in this session
	 */
	public int newMessage(String message, int type) {
		return newMessage(message, type, NO_DELAY);
	}

	/**
	 * Add a new message
	 * 
	 * @param message
	 *            the new incoming message
	 * @param type
	 *            the message type
	 * @param delay
	 *            the delay this message is valid
	 * @return the message id in this session
	 */
	public int newMessage(String text, int type, int delay) {
		ImogMessage message = createMessage(type, delay, text);
		notifyIncoming(message);
		return message.getId();
	}

	/**
	 * Remove a message
	 * 
	 * @param id
	 */
	public void invalidate(int id) {
		notifyInvalidate(id);
	}

	/**
	 * Notify listeners from an incoming message.
	 * 
	 * @param message
	 *            the message text
	 * @param type
	 *            the message type
	 */
	private void notifyIncoming(ImogMessage message) {
		for (MessageListener listener : listeners) {
			if (listener != null)
				listener.incoming(message);
		}
	}

	/**
	 * Notify listeners that the specified message is no more useful.
	 * 
	 * @param messageId
	 */
	private void notifyInvalidate(int messageId) {
		for (MessageListener listener : listeners) {
			if (listener != null)
				listener.invalidate(messageId);
		}
	}

	/**
	 * Create a new message
	 * 
	 * @param type
	 *            the message type
	 * @param delay
	 *            the message delay
	 * @param text
	 *            the message text
	 * @return the created message
	 */
	private ImogMessage createMessage(int type, int delay, String text) {
		messageCounter++;
		return new ImogMessage(messageCounter, type, text, delay);
	}

	/* INTERNAL CLASSES */

	/**
	 * A internal message to inform the user
	 */
	public static class ImogMessage {

		private int id;
		private int type;
		private int delay;
		private String text;

		public ImogMessage(int pId, int pType, String pText, int pDelay) {
			id = pId;
			type = pType;
			text = pText;
			delay = pDelay;
		}

		public int getId() {
			return id;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getDelay() {
			return delay;
		}

		public void setDelay(int delay) {
			this.delay = delay;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

	/**
	 * Notified when a new message is added.
	 */
	public interface MessageListener {

		public void incoming(ImogMessage message);

		public void invalidate(int id);
	}

}
