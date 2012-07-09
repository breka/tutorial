package org.imogene.web.gwt.client.ui.panel;

import org.imogene.web.gwt.client.ui.MessageManager;
import org.imogene.web.gwt.client.ui.MessageManager.ImogMessage;
import org.imogene.web.gwt.client.ui.MessageManager.MessageListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;


/**
 * Panel that permits to the application 
 * to display global messages to the user.
 * @author Medes-IMPS
 */
public class MessagePanel extends Composite implements MessageListener {
	
	private static String DEFAULT_STYLE = "lightblue";
	
	private static String WARNING_STYLE = "orange";
	
	private static String ERROR_STYLE = "red";
	
	private DecoratorPanel deco;
	
	private HorizontalPanel messageLayout;
	
	private Image messageIcon;
	
	private HTML messageLabel;
	
	private ImogMessage displayed;	
	
	public MessagePanel(){
		layout();
	}
	
	private void layout(){
		deco = new DecoratorPanel();
		messageLayout = new HorizontalPanel();
		messageIcon = new Image(GWT.getModuleBaseURL()+"images/msg_info-16.png");
		messageLabel = new HTML();
		messageLabel.setHTML("");
		messageLayout.add(messageIcon);
		messageLayout.add(messageLabel);		
		deco.setWidget(messageLayout);
		initWidget(deco);
		properties();
	}
	
	private void properties(){
		messageLabel.setStylePrimaryName("imogene-ImogMessage");
		messageLabel.addStyleDependentName(DEFAULT_STYLE);
		messageLabel.setWidth("100%");
		messageLabel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		messageLayout.setWidth("100%");
		messageLayout.setCellWidth(messageIcon, "16px");
		messageLayout.setCellVerticalAlignment(messageIcon, HorizontalPanel.ALIGN_MIDDLE);
		messageLayout.setStylePrimaryName("imogene-ImogMessage");
		messageLayout.addStyleDependentName(DEFAULT_STYLE);
		deco.setStylePrimaryName("imogene-DecoratorPanel");
		deco.addStyleDependentName(DEFAULT_STYLE);
		setVisible(false);
		MessageManager.get().addMessageListener(this);
	}	

	@Override
	public void incoming(ImogMessage message) {		
		displayed = message;
		messageLabel.setHTML(displayed.getText().replace("\n", "<br>"));
		switch(displayed.getType()){
		case MessageManager.INFO_TYPE:
			setStyle(DEFAULT_STYLE);
			break;
		case MessageManager.ERROR_TYPE:
			setStyle(ERROR_STYLE);
			break;
		case MessageManager.WARNING_TYPE:
			setStyle(WARNING_STYLE);
			break;
		default:
			setStyle(DEFAULT_STYLE);			
		}
		setVisible(true);
		if(message.getDelay()>-1){
			new MessageTimer(this, message.getId(), message.getDelay());
		}
	}

	@Override
	public void invalidate(int id) {	
		if(displayed.getId()==id){
			messageLabel.setHTML("");
			setVisible(false);			
		}
	}
	
	private void setStyle(String style){
		if(!style.equals(DEFAULT_STYLE))	
			removeStyle(DEFAULT_STYLE);
		if(!style.equals(WARNING_STYLE))
			removeStyle(WARNING_STYLE);
		if(!style.equals(ERROR_STYLE))
			removeStyle(ERROR_STYLE);		
		addStyle(style);
		setImage(style);
	}
	
	private void removeStyle(String style){
		messageLabel.removeStyleDependentName(style);
		messageLayout.removeStyleDependentName(style);
		deco.removeStyleDependentName(style);
	}
	
	private void addStyle(String style){
		messageLabel.addStyleDependentName(style);
		messageLayout.addStyleDependentName(style);
		deco.addStyleDependentName(style);
	}
	
	private void setImage(String style){
		String url_base = GWT.getModuleBaseURL()+"images/";
		String urlSuffix = "msg_info-16.png";
		if(WARNING_STYLE.equals(style))
			urlSuffix = "msg_warn-16.png";
		if(ERROR_STYLE.equals(style))
			urlSuffix = "msg_error-16.png";
		messageIcon.setUrl(url_base+urlSuffix);
	}
	
	/* INTERNAL CLASSES */
	
	private static class MessageTimer extends Timer {

		private int messageId;
		
		private MessageListener thisListener;
		
		public MessageTimer(MessageListener listener, int id, int delay){
			messageId = id;
			thisListener = listener;
			schedule(delay*1000);
		}
		
		@Override
		public void run() {			
			thisListener.invalidate(messageId);
		}
		
	}
	
}
