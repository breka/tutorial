package org.imogene.web.gwt.client.ui.panel;

import java.util.Date;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.i18n.TextFormatUtil;
import org.imogene.web.gwt.common.entity.ImogActor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;


public class TopBannerPanel extends Composite {	
	
	private static final String DEFAULT_LOGO = GWT.getModuleBaseURL()+ "/images/logo_medoo.png";

	private VerticalPanel layout;	
	private HorizontalPanel topPanel;	
	private HorizontalPanel logoPanel;	
	private MessagePanel messagePanel;	
	private HTML lastConnection;	
	private Image logo = new Image(GWT.getModuleBaseURL()+ "/images/logo_medoo.png");		
	private Hyperlink logout;
	
	
	
	public TopBannerPanel(){		
		this(DEFAULT_LOGO);
	}
	
	public TopBannerPanel(String pLogoUrl){
		logo = new Image(pLogoUrl);	
		layout();
		properties();
	}
	
	/**
	 */
	private void layout(){
		layout = new VerticalPanel();
		topPanel = new HorizontalPanel();	
		lastConnection = new HTML();
		messagePanel = new MessagePanel();
		topPanel.add(lastConnection);
		logout = new Hyperlink(BaseNLS.constants().disconnect(),"logout");
		topPanel.add(logout);
		
		logoPanel = new HorizontalPanel();
		logoPanel.add(logo);		
		logoPanel.add(messagePanel);
		
		layout.add(topPanel);
		layout.add(logoPanel);
		initWidget(layout);
	}
	
	/**
	 */
	private void properties(){		
		topPanel.setSpacing(5);
		topPanel.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
		topPanel.setVerticalAlignment(HasAlignment.ALIGN_BOTTOM);
		topPanel.setCellHorizontalAlignment(lastConnection, HorizontalPanel.ALIGN_RIGHT);
		topPanel.setCellVerticalAlignment(lastConnection, HasVerticalAlignment.ALIGN_BOTTOM);
		topPanel.setCellWidth(lastConnection, "100%");
		topPanel.setCellHorizontalAlignment(logout, HorizontalPanel.ALIGN_RIGHT);
		topPanel.setCellVerticalAlignment(logout, HasVerticalAlignment.ALIGN_BOTTOM);
		topPanel.setStylePrimaryName("imogene-MainHeader");	
		
		logoPanel.setSpacing(0);
		logoPanel.setStylePrimaryName("imogene-Logo");
		logoPanel.setWidth("100%");
		//logoPanel.setCellWidth(logo, "20%");	
		//logoPanel.setCellWidth(messagePanel, "300px");
		messagePanel.setWidth("300px");		
		logoPanel.setCellVerticalAlignment(messagePanel, HorizontalPanel.ALIGN_MIDDLE);
		logoPanel.setCellHorizontalAlignment(messagePanel, HorizontalPanel.ALIGN_LEFT);
		layout.setWidth("100%");
		logout.setStyleName("imogene-logout");
		lastConnection.setStyleName("imogene-greytext");
	}
	
	/**
	 * Set panel information relative to the actor 
	 * @param actor the current actor
	 */
	public void setActor(ImogActor actor){
		Date cDate = actor.getLastLoginDate();
		if(cDate == null) cDate = new Date();
		String lastLoginDate = TextFormatUtil.getDateTime(cDate);
		String metadata = BaseNLS.messages().form_metadata_current_user(actor.getLogin(), lastLoginDate);
		lastConnection.setHTML(metadata);
	}
}
