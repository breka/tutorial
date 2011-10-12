package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.BinaryDesc;
import org.imogene.web.gwt.client.BinaryTools;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.ui.field.upload.ImogBinaryUploader;
import org.imogene.web.gwt.remote.UtilServicesAsyncFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;



/**
 * Composite to manage the display of Binary fields
 * @author Medes-IMPS
 */
public class ImogBinaryField extends ImogFieldAbstract<String> implements ImogUploader {

	/* status */	
	private String thisLabel;	
	private String thisValue;		
	private boolean edited = false;
	
	/* widgets */	
	private HorizontalPanel main;	
	private Image fileIcon = new Image(GWT.getModuleBaseURL()+ "images/file_icon_32.png");
	
	/* edit panel */
	private VerticalPanel editPanel;	
	private ImogBinaryUploader uploader;
	
	/* display panel */
	private HorizontalPanel infoPanel;
	private VerticalPanel downloadPanel;			
	private HTML downloadLink;	
	private HTML nameLabel;	
	private HTML sizeLabel;
	
	
	
	public ImogBinaryField(){
		layout();
	}
	
	public ImogBinaryField(String title){
		this();
		thisLabel = title;
	}	
	
	private void layout(){
		
		main = new HorizontalPanel();
		main.add(fileIcon);
		
		/* info panel */	
		infoPanel = new HorizontalPanel();
		downloadPanel = new VerticalPanel();
		nameLabel = new HTML(BaseNLS.messages().field_binary_file(BaseNLS.constants().binary_nofile()));
		sizeLabel = new HTML(BaseNLS.messages().field_binary_size("-"));
		downloadPanel.add(nameLabel);
		downloadPanel.add(sizeLabel);
		downloadLink = new HTML();
		infoPanel.add(downloadPanel);
		infoPanel.add(downloadLink);
		
		/* edit panel */
		editPanel = new VerticalPanel();		
		uploader = new ImogBinaryUploader();			
		editPanel.add(uploader);
		
		main.add(infoPanel);
		initWidget(main);
		properties();
	}
	
	private void properties(){		
		main.setSpacing(3);
		main.setWidth("100%");
		main.setCellWidth(fileIcon, "32px");
		main.setCellVerticalAlignment(fileIcon, HorizontalPanel.ALIGN_MIDDLE);	
		infoPanel.setSpacing(3);
		nameLabel.setStyleName("imogene-greytext");
		sizeLabel.setStyleName("imogene-greytext");
	}
	
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getLabel() {
		if(thisLabel != null)
			return thisLabel;
		return "";
	}

	@Override
	public String getValue() {
		if(uploader.getEntityId()!=null){
			return uploader.getEntityId();
		}
		return thisValue;
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
		
	}

	@Override
	public void setValue(String value) {
		if(value!=null && !"".equals(value)){
			downloadLink.setHTML(BinaryTools.DOWNLOAD_TMPL.replace("%PARAM_ID%",value));
			if(value!=thisValue)
				setBinaryMetadata(value);
		}	
		thisValue = value;		
	}
	
	@Override
	public void setValue(String value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}

	@Override
	public void setEnabled(boolean editable) {
		if(!edited && editable){
			main.remove(infoPanel);
			main.add(editPanel);			
		}
		if(edited && !editable){
			main.remove(editPanel);
			main.add(infoPanel);
		}	
		edited = editable;
	}

	@Override
	public boolean isEdited() {
		return edited;
	}

	@Override
	public boolean isUploading() {		
		return uploader.isUploading();
	}	
	
	/**
	 * display the binary meta-data
	 */
	public void setBinaryMetadata(String value){
		UtilServicesAsyncFacade.getInstance().getBinaryDesc(value, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {	
				BinaryDesc binaryDesc = BinaryTools.createBinaryDesc(result);
				nameLabel.setHTML(BaseNLS.messages().field_binary_file(binaryDesc.getName()));
				sizeLabel.setHTML(BaseNLS.messages().field_binary_size(BinaryTools.getSizeAsString(binaryDesc.getSize())));
			}
			
			@Override
			public void onFailure(Throwable arg0) {
			}
		});
	}
	
}
