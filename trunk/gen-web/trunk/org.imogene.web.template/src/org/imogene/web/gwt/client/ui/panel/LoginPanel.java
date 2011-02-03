package org.imogene.web.gwt.client.ui.panel;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.ui.field.ImogPasswordBox;
import org.imogene.web.gwt.client.ui.field.ImogTextBox;
import org.imogene.web.gwt.common.entity.ImogActor;
import org.imogene.web.gwt.remote.AuthenticationServiceAsyncFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Login panel implementation
 * @author Medes-IMPS
 */
public class LoginPanel extends Composite {
	
	private static final String DEFAULT_LOGO = GWT.getModuleBaseURL()+ "/images/logo_web.png";
	
	private String logoUrl;	
	private AbsolutePanel absPanel;	
	private VerticalPanel contentPanel;
	private SimplePanel formPanel;
	private Grid gridPanel;		
	private SimplePanel imagePanel;
	private HorizontalPanel langPanel;
	private Image logo;
	
	private Label messages;
	private Label loginLabel;
	private Label passwdLabel;	
	private ImogTextBox login;
	private ImogPasswordBox passwd;
	private Button loginButton;
	
	private Image loadingImage;
	
	private HTMLPanel descriptionPanel;
	
	private AuthenticationListener listener;
	
	/**
	 */
	public LoginPanel(){
		this(DEFAULT_LOGO);
	}
	
	public LoginPanel(String pLogoUrl){
		this(pLogoUrl, false);
	}
	
	public LoginPanel(String pLogoUrl, boolean hasBeenDeconnected){
		logoUrl = pLogoUrl;
		layout(hasBeenDeconnected);
	}
	
	/**
	 */
	private void layout(boolean hasBeenDeconnected){		
		formPanel = new SimplePanel();
		absPanel = new AbsolutePanel();	
		
		/* content */		
		contentPanel = new VerticalPanel();
		gridPanel = new Grid(4,2);
		formPanel.setWidget(gridPanel);
		contentPanel.add(formPanel);
		absPanel.add(contentPanel, 75, 15);
		
		/* error message */
		messages = new Label();
		gridPanel.setWidget(0, 1, messages);
		
		/* login information */
		loginLabel = new Label(BaseNLS.constants().login_login());
		login = new ImogTextBox();
		gridPanel.setWidget(1, 0, loginLabel);
		gridPanel.setWidget(1, 1, login);
		
		passwdLabel = new Label(BaseNLS.constants().login_password());
		passwd = new ImogPasswordBox();
		gridPanel.setWidget(2, 0, passwdLabel);
		gridPanel.setWidget(2, 1, passwd);
		
		/* loading image */
		loadingImage = new Image(GWT.getModuleBaseURL()+ "/images/loading.gif");
		gridPanel.setWidget(3, 0, loadingImage);
		
		/* login button */
		loginButton = new Button(BaseNLS.constants().login_button());
		gridPanel.setWidget(3, 1, loginButton);	
		
		/* logo */
		imagePanel = new SimplePanel();
		logo = new Image(logoUrl);
		imagePanel.setWidget(logo);
		absPanel.add(imagePanel, 0, 0);
		
		/* language panel */				
		langPanel = new HorizontalPanel();
		contentPanel.add(langPanel);
		
		/* description panel */
		String desc = BaseNLS.constants().login_description();
		if (!desc.equals("")) {
			descriptionPanel = new HTMLPanel(BaseNLS.constants().login_description());
			absPanel.add(descriptionPanel, 150, 200);
		}
				
		initWidget(absPanel);
		properties(hasBeenDeconnected);
		behavior();
	}
	
	/**
	 */
	private void properties(boolean hasBeenDeconnected){
		loadingImage.setVisible(false);
		imagePanel.setStylePrimaryName("imogene-LoginImage");
		absPanel.setStylePrimaryName("imogene-Login");
		formPanel.setStylePrimaryName("imogene-LoginPanel");		
		loginButton.setStyleName("Login-Button");			
		messages.setStylePrimaryName("imogene-LoginMessage");
		messages.setWordWrap(true);	
		loginLabel.setStylePrimaryName("imogene-LoginBox");
		passwdLabel.setStylePrimaryName("imogene-LoginBox");
		if(hasBeenDeconnected)
			messages.setText(BaseNLS.constants().login_expired());
		else
			messages.setText(BaseNLS.constants().login_identify());
		if (descriptionPanel!=null)
			descriptionPanel.setStylePrimaryName("imogene-LoginDescriptionPanel");	
		
		contentPanel.setCellHorizontalAlignment(langPanel, HorizontalPanel.ALIGN_RIGHT);
	}
	
	/**
	 */
	private void behavior(){
		loginButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				AuthenticationServiceAsyncFacade.getInstance().validateLogin(login.getEmbedded().getText(), passwd.getEmbedded().getText(), new LoginResultCallback());
				loginButton.setVisible(false);
				loadingImage.setVisible(true);
			}
		});
	}
	
	/**
	 * Clear all messages and inputs of this panel
	 */
	public void clear(){
		login.getEmbedded().setText("");
		passwd.getEmbedded().setText("");
		messages.setText(BaseNLS.constants().login_identify());
	}
	
	/**
	 * Sets the authentication listener, which will 
	 * be notified after an authentication operation.
	 * @param pListener the listener to set
	 */
	public void setAuthenticationListener(AuthenticationListener pListener){
		listener = pListener;
	}
	
	/**
	 * Removes the currently defined authentication listener
	 */
	public void removeAuthenticationListener(){
		listener = null;
	}
	
	/**
	 * Add a supported laguage
	 * @param name
	 * @param isoCode
	 */
	public void addLanguage(String name, String isoCode){
		StringBuilder html = new StringBuilder("<a href=\"?locale="+isoCode+"\">"+name+"</a>");
		html.append("&nbsp;");
		HTML lang = new HTML(html.toString());
		langPanel.add(lang);
	}
	
	/** PRIVATE CLASSES */
	
	/**
	 * CallBack for the login validation result.
	 * @author Medes-IMPS
	 */
	private class LoginResultCallback implements AsyncCallback<ImogActor>{

		@Override
		public void onFailure(Throwable caught) {			
			Window.alert("Une erreur est survenue lors de la connexion au serveur.\n" + caught.getMessage());
			loadingImage.setVisible(false);
			loginButton.setVisible(true);
		}

		@Override
		public void onSuccess(ImogActor actor) {
			if(actor!=null){
				if(listener != null)
					listener.authenticated(actor);
			}else{
				messages.setText(BaseNLS.constants().login_wrong_id());
				passwd.getEmbedded().setText("");
				
			}
			loadingImage.setVisible(false);
			loginButton.setVisible(true);
		}
		
	}
	
	/**
	 * Interface for the client that will be notified 
	 * when the authentication process is successful.
	 * @author Medes-IMPS
	 */
	public interface AuthenticationListener {
		
		public void authenticated(ImogActor actor);
	}
	

}
