package org.imogene.rcp.core.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.wrapper.CoreMessages;


/**
 * View that displays the application help.
 * @author Medes-IMPS
 */
public class HelpView extends ViewPart {

	public static String ID = "Imogene.HelpView";
	
	private Browser browser;
	
	private Image backImage = ImogPlugin.getImageDescriptor("icons/back_icon_24.png").createImage();
	
	private Image forwImage = ImogPlugin.getImageDescriptor("icons/forw_icon_24.png").createImage();
	
	@Override
	public void createPartControl(Composite parent) {
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		parent.setLayout(layout);
		createTopButtonComposite(parent);
		browser = new Browser(parent, SWT.BORDER);		
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	@Override
	public void setFocus() {		
		
	}
	
	/**
	 * Create a composite to propose the navigation buttons. 
	 * @param parent the parent composite.
	 */
	private void createTopButtonComposite(Composite parent){
		Composite composite = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL );
		layout.marginWidth = 0;
		layout.spacing = 2;
		composite.setLayout(layout);
		Button backButton = new Button(composite, SWT.PUSH);
		backButton.setImage(backImage);
		backButton.setToolTipText(CoreMessages.getString("help_back"));
		backButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {				
				super.widgetSelected(e);
				browser.back();
			}
		});
		Button forwButton = new Button(composite, SWT.PUSH);
		forwButton.setImage(forwImage);
		forwButton.setToolTipText(CoreMessages.getString("help_forward"));
		forwButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {				
				super.widgetSelected(e);
				browser.forward();
			}
		});
		composite.addDisposeListener(new DisposeListener(){
			
			public void widgetDisposed(DisposeEvent arg0) {
				backImage.dispose();
				forwImage.dispose();
			}
		});
		
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		composite.setLayoutData(data);
	}
	
	/**
	 * Open the specified URL
	 * @param url the url to open
	 */
	public void openUrl(String url){
		if(browser!=null)
			browser.setUrl(url);
	}
	

}
