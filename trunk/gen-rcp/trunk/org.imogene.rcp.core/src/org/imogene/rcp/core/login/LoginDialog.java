package org.imogene.rcp.core.login;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.wrapper.CoreMessages;


/**
 * Shell that displays the Imogene login screen
 * @author MEDES-IMPS
 */
public class LoginDialog {

	public static int ERROR_INVALID_FILE=1;	
	public static int WARNING_ALREADY_EXISTS=2;	
	public static int OK_ADDED;	
	
	private Display display;
	private Shell shell;
	private FormToolkit toolkit;
	private Form form;
	private Combo loginCombo;
	private Text passwdText;
	private Button loginButton;
	private Button uploadButton;
	private Text filePathText;	
	private Section loginSection;
	
	private boolean authenticate = false;
	
	private Map<String, Identity> identities = new HashMap<String, Identity>();
	

	/**
	 * Creates the login dialog
	 * @param display the parent display
	 */
	public LoginDialog(Display display) {
		this.display = display;
		createContents();
		loadIdentities();	
		shell.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR)
					loginAction();
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
			
		});
	}

	/**
	 * create the content
	 */
	private void createContents() {
		shell = new Shell(display, SWT.BORDER | SWT.CLOSE);
		final FillLayout fillLayout = new FillLayout();
		shell.setLayout(fillLayout);		
		shell.setSize(600, 375);	
		shell.setText(CoreMessages.getString("login_title"));
		toolkit = new FormToolkit(display);
		form = toolkit.createForm(shell);
		form.setText(CoreMessages.getString("core_welcome"));
		Composite body = form.getBody();
		
		GridLayout bodyLayout = new GridLayout(2, false);		
		body.setLayout(bodyLayout);
		createLeftCorner(body);
		createLoginSection(body);
		createIdSection(body);
	}

	/**
	 * 
	 * @param parent
	 */
	private void createLeftCorner(Composite parent){
		Label leftCorner = toolkit.createLabel(parent, "");
		Image leftImage = ImogPlugin.getImageDescriptor("icons/epidefender-login.png").createImage();
		leftCorner.setImage(leftImage);
		
		GridData cornerData = new GridData(GridData.FILL_BOTH);
		cornerData.verticalAlignment=GridData.VERTICAL_ALIGN_CENTER;
		cornerData.verticalSpan=2;
		leftCorner.setLayoutData(cornerData);
		
	}
	
	/**
	 * Create the the login section
	 * @param parent the parent composite
	 */
	private void createLoginSection(Composite parent) {
		loginSection = toolkit.createSection(parent,
				Section.DESCRIPTION | Section.TITLE_BAR | Section.EXPANDED);
		loginSection.setText(CoreMessages.getString("login_text"));
		loginSection
				.setDescription(CoreMessages.getString("login_desc"));
		Composite sectionClient = toolkit.createComposite(loginSection);
		sectionClient.setLayout(new GridLayout(3, false));
		loginSection.setClient(sectionClient);
		
		GridData sectionData = new GridData(GridData.FILL_HORIZONTAL);
		sectionData.horizontalAlignment = SWT.RIGHT;
		loginSection.setLayoutData(sectionData);

		/* logo label */
		Label logoLabel = new Label(sectionClient, SWT.NONE);
		Image logoImage = ImogPlugin.getImageDescriptor(
				"icons/doctor-32x32.gif").createImage();
		logoLabel.setImage(logoImage);
		GridData logoData = new GridData();
		logoData.verticalSpan = 3;
		logoData.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;
		logoLabel.setLayoutData(logoData);

		/* login part */
		toolkit.createLabel(sectionClient, CoreMessages.getString("login_label") + ": ");
		loginCombo = new Combo(sectionClient, SWT.FLAT | SWT.READ_ONLY);		
		GridData loginData = new GridData(GridData.FILL_HORIZONTAL);
		loginData.minimumWidth = 200;
		loginCombo.setLayoutData(loginData);		

		/* password part */
		toolkit.createLabel(sectionClient, CoreMessages.getString("passswd_label") + ": ");
		passwdText = toolkit.createText(sectionClient, "");
		passwdText.setEchoChar('*');
		GridData passwdData = new GridData(GridData.FILL_HORIZONTAL);
		passwdData.widthHint = 100;
		passwdText.setLayoutData(passwdData);

		/* login button part */
		loginButton = toolkit.createButton(sectionClient, CoreMessages.getString("login_button"), SWT.PUSH
				| SWT.FLAT);
		GridData buttonData = new GridData();
		buttonData.horizontalAlignment = SWT.RIGHT;
		buttonData.horizontalSpan = 3;
		loginButton.setLayoutData(buttonData);
		loginButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loginAction();			
			}
		});		

		toolkit.paintBordersFor(sectionClient);
	}
	
	private void loginAction(){
		String selectedId = (String)loginCombo.getData(String.valueOf(loginCombo.getSelectionIndex()));
		Identity selIdentity = identities.get(selectedId);
		if(selIdentity!=null && selIdentity.getPassword().equals(passwdText.getText())){
			authenticate = true;
			ImogPlugin plugin = ImogPlugin.getDefault();
			plugin.setCurrentUserIdentity(selIdentity);
			startSynchro();
			shell.close();
		}else{
			MessageDialog.openWarning(shell, CoreMessages.getString("login_error_dialog_title"), CoreMessages.getString("login_error_dialog_text"));
			passwdText.setText("");
		}	
	}

	/**
	 * Create the section that display widgets that permits to upload an
	 * identity file.
	 * @param parent the parent composite
	 */
	private void createIdSection(Composite parent) {
		Section idSection = toolkit.createSection(parent, Section.DESCRIPTION
				| Section.TITLE_BAR | Section.EXPANDED);
		idSection.setText(CoreMessages.getString("identity_text"));
		idSection
				.setDescription(CoreMessages.getString("identity_desc"));
		Composite sectionClient = toolkit.createComposite(idSection);
		sectionClient.setLayout(new GridLayout(3, false));
		idSection.setClient(sectionClient);
		
		GridData sectionData = new GridData(GridData.FILL_HORIZONTAL);
		sectionData.horizontalAlignment = SWT.RIGHT;
		sectionData.horizontalSpan=2;
		idSection.setLayoutData(sectionData);

		
		
		/* logo label */
		Label logoLabel = toolkit.createLabel(sectionClient, "");
		Image logoImage = ImogPlugin.getImageDescriptor(
				"icons/fileid-32x32.gif").createImage();
		logoLabel.setImage(logoImage);
		GridData logoData = new GridData();
		logoData.verticalSpan = 2;
		logoLabel.setLayoutData(logoData);

		/* file path text */
		filePathText = toolkit.createText(sectionClient, "");
		filePathText.setEditable(false);
		GridData fileData = new GridData();
		fileData.widthHint = 200;
		filePathText.setLayoutData(fileData);

		/* file chooser button */
		Button fileChooserButton = toolkit.createButton(sectionClient,
				CoreMessages.getString("identity_button_select"), SWT.PUSH | SWT.FLAT);
		fileChooserButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(Display.getCurrent()
						.getActiveShell());
				dialog.setFilterExtensions(new String[] { "*.imogid" });
				String resultPath = dialog.open();
				if(resultPath!=null){				
					filePathText.setText(resultPath);
					uploadButton.setEnabled(true);
				}
			}

		});

		/* upload button */
		uploadButton = toolkit.createButton(sectionClient, CoreMessages.getString("identity_button_upload"),
				SWT.PUSH | SWT.FLAT);
		GridData buttonData = new GridData();
		buttonData.horizontalAlignment = SWT.RIGHT;
		buttonData.horizontalSpan = 3;
		uploadButton.setLayoutData(buttonData);
		uploadButton.setEnabled(false);
		uploadButton.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(filePathText.getText()!=""){
					int result = addIdentity(filePathText.getText());
					if(result == OK_ADDED)
						MessageDialog.openInformation(shell, CoreMessages.getString("identity_message_ok_title"), CoreMessages.getString("identity_message_ok_text"));
					else if(result == WARNING_ALREADY_EXISTS)
						MessageDialog.openWarning(shell, CoreMessages.getString("identity_message_warn_title"), CoreMessages.getString("identity_message_warn_text"));
					else
						MessageDialog.openError(shell, CoreMessages.getString("identity_message_error_title"), CoreMessages.getString("identity_message_eror_text"));
				}
				filePathText.setText("");
			}
			
		});

		toolkit.paintBordersFor(sectionClient);

	}
	
	/**
	 * Open the shell to authenticate the user.
	 * @return true if success false otherwise
	 */
	public boolean authenticate(){
		//Location location = Platform.getInstanceLocation();		
		//System.out.println(location.getURL());		
		loadIdentities();
		shell.setLocation(320, 290);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return authenticate;
	}
	
	/**
	 * Fill the combo list that display identities
	 * @param list
	 */
	private void fillIdentList(){
		loginCombo.removeAll();
		for(Identity identity: identities.values()){
			int index=loginCombo.getItemCount();
			loginCombo.add(identity.getLogin());
			loginCombo.setData(String.valueOf(index), identity.getId());
		}
	}
	
	/**
	 * Load identities from existing 
	 * identity files.
	 */
	private void loadIdentities(){
		Location location = Platform.getInstanceLocation();
		File identity = new File(location.getURL().getPath().concat("/identity"));
		if(!identity.exists()){
			identity.mkdir();			
		}
		File[] files = identity.listFiles();
		for(File idFile:files){
			Identity current = IdentityHelper.loadFromFile((idFile));
			identities.put(current.getId(), current);
		}
		fillIdentList();		
	}
	
	/**
	 * Add an identity to the application
	 * @param filePath the path to the file to add.
	 */
	private int addIdentity(String filePath){
		File file = new File(filePath);
		if(IdentityHelper.validateIdFile(file)){
			Identity newIdent = IdentityHelper.loadFromFile(file);
			if(!identities.containsKey(newIdent.getId())) {
				Location location = Platform.getInstanceLocation();
				File dFile = new File(location.getURL().getPath().concat(
						"/identity/" + UUID.randomUUID() + ".imogid"));
				IdentityHelper.copyfile(file, dFile);
				loadIdentities();
				fillIdentList();
				return OK_ADDED;
			}
			return WARNING_ALREADY_EXISTS;
		}
		return ERROR_INVALID_FILE;
	}
	
	/**
	 * Starts the synchronisation process if it has been defined as automatic
	 */
	private void startSynchro() {
		ImogPlugin plugin = ImogPlugin.getDefault();	
		if(plugin.getPreferenceStore().getBoolean("SYNC_LOOP"))
			plugin.startSynchro();
	}
}
