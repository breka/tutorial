package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.ui.form.DisclosureContainerComposite;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Specific section panel to display association N
 * @author Medes-IMPS
 */
public class MultiRelationsField extends DisclosureContainerComposite {	
	
	private HorizontalPanel computingPanel;
	
	private VerticalPanel linkList;
	
	private Widget content;
	
	public MultiRelationsField(String label){
		super(label);		
		layout();
	}
	
	/** main layout */
	private void layout(){				
		layoutComputing();
		setContent(computingPanel);	
		properties();
	}	
	
	private void properties(){	
		propertiesComputing();
		setActivated(false);
	}
	
	/** computing part */
	private void layoutComputing(){
		linkList = new VerticalPanel();
		computingPanel = new HorizontalPanel();
		Image flower = new Image(GWT.getModuleBaseURL()+"/images/loading.gif");
		computingPanel.add(flower);
		Label computingLabel = new Label("Téléchargement ...");
		computingPanel.add(computingLabel);
	}	
	
	private void propertiesComputing(){		
	}			
	
	public void setEnabled(boolean enabled){
		if(enabled){
			super.setContent(new Label("cette section n'est pas editable"));
		}else{
			super.setContent(content);
		}
	}
	
	/**
	 * add a link to the panel.
	 * @param link link to the references entity view
	 */
	public void add(Hyperlink link){
		linkList.add(link);					
	}

	@Override
	public void setContent(Widget pContent) {
		content = pContent;
		super.setContent(content);		
	}

	/**
	 * Display the link list.
	 */
	public void displayResult() {		
		setContent(linkList);
	}
	
	
}
