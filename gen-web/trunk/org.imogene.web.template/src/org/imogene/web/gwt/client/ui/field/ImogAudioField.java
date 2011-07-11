package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.BinaryDesc;
import org.imogene.web.gwt.client.BinaryTools;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.ui.field.upload.ImogBinaryUploader;
import org.imogene.web.gwt.remote.UtilServicesAsyncFacade;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.allen_sauer.gwt.voices.client.handler.PlaybackCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Widgets that permits to display an edit an audio file.
 * This widget proposes to download the audio file or to
 * play it in an embedded flash player
 * @author Medes-IMPS
 */
public class ImogAudioField extends ImogFieldAbstract<String> implements ClickHandler, SoundHandler, ImogUploader {

	private static String PLAYER_PLAY = GWT.getModuleBaseURL()+"images/sound_play_32.png";	
	private static String PLAYER_STOP = GWT.getModuleBaseURL()+"images/sound_stop_32.png";	
	private static String PLAYER_DISABLED = GWT.getModuleBaseURL()+"images/sound_disable_32.png";	
	
	/* status */		
	private String thisLabel;	
	private String thisValue;	
	private boolean thisEditable = false;
	private boolean playing = false;
	private SoundController sndController;
	private Sound sound;
	
	/* widgets common */
	private HorizontalPanel main;
	private Image playerStatus;
	
	/* widgets edit */
	private VerticalPanel editPanel;
	private ImogBinaryUploader uploader;
	
	/* widgets view */
	private HorizontalPanel infoPanel;
	private VerticalPanel downloadPanel;		
	private HTML nameLabel;
	private HTML sizeLabel;
	private HTML downloadLink;
		
	/**
	 * Create an audio field 
	 * with an empty label
	 */
	public ImogAudioField(){
		layout();
	}
	
	/**
	 * Create an audio field
	 * with the specified label
	 * @param label the field label
	 */
	public ImogAudioField(String label){
		this();
		thisLabel = label;
	}
	
	/**
	 * Layout the composite
	 */
	private void layout(){
		main = new HorizontalPanel();
		playerStatus = new Image(PLAYER_DISABLED);
		
		/* display */
		infoPanel = new HorizontalPanel();
		downloadPanel = new VerticalPanel();
		downloadLink = new HTML();	
		nameLabel = new HTML(BaseNLS.messages().field_binary_file(BaseNLS.constants().binary_nofile()));
		sizeLabel = new HTML(BaseNLS.messages().field_binary_size("-"));			
		downloadPanel.add(nameLabel);
		downloadPanel.add(sizeLabel);
		infoPanel.add(downloadPanel);
		infoPanel.add(downloadLink);		
		
		/* editPanel */
		editPanel = new VerticalPanel();		
		uploader = new ImogBinaryUploader();			
		editPanel.add(uploader);	
		
		main.add(playerStatus);
		main.add(infoPanel);
		initWidget(main);
		properties();
	}
	
	/**
	 * Set the widget properties
	 */
	private void properties(){	
		main.setSpacing(3);
		main.setWidth("100%");
		main.setCellWidth(playerStatus, "32px");
		main.setCellVerticalAlignment(playerStatus, HorizontalPanel.ALIGN_MIDDLE);
		playerStatus.addClickHandler(this);
		infoPanel.setSpacing(3);	
		nameLabel.setStyleName("imogene-greytext");
		sizeLabel.setStyleName("imogene-greytext");
	}
	
	@Override
	public boolean validate() {	
		
		return true;
	}

	@Override
	public String getLabel() {
		if(thisLabel != null)
			return thisLabel;
		return "";
	}

	/**
	 * @return The binary audio file entity ID
	 */
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
			/* sound controller */
			playerStatus.setUrl(PLAYER_PLAY);
			playerStatus.setStylePrimaryName("imogene-ImageLink");
			sndController = new SoundController();
			sound = sndController.createSound(Sound.MIME_TYPE_AUDIO_MPEG,
		        GWT.getHostPageBaseURL()+"getbinary?mp3Id="+value);			
			sound.addEventHandler(this);
			/* download controller */
			downloadLink.setHTML(BinaryTools.DOWNLOAD_TMPL.replace("%PARAM_ID%", value));
			if(thisValue!=value)
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
		if(!thisEditable && editable){
			main.remove(infoPanel);
			main.add(editPanel);			
		}
		if(thisEditable && !editable){
			main.remove(editPanel);
			main.add(infoPanel);
		}
		thisEditable = editable;
		setPlayerStatus();		
	}		
	
	@Override
	public void onClick(ClickEvent event) {		
		if(event.getSource().equals(playerStatus)){
			if(sound!=null && !playing){
				sound.play();
				playing = true;
				playerStatus.setUrl(PLAYER_STOP);
				GWT.log("Start playing the sound",null);
				return;
			}
			if(sound != null && playing){
				sound.stop();
				playing =false;
				playerStatus.setUrl(PLAYER_PLAY);
				GWT.log("Stop playing the sound",null);
				return;
			}
		}		
	}	

	@Override
	public void onPlaybackComplete(PlaybackCompleteEvent event) {		
		playing = false;
		playerStatus.setUrl(PLAYER_PLAY);
	}

	@Override
	public void onSoundLoadStateChange(SoundLoadStateChangeEvent event) {		
		
	}

	@Override
	public boolean isEdited() {		
		return thisEditable;
	}
	
	@Override
	public boolean isUploading() {		
		return uploader.isUploading();
	}

	/**
	 * Set the player status (icon and behavior)
	 */
	private void setPlayerStatus(){
		if(thisEditable){
			playerStatus.setUrl(PLAYER_DISABLED);
		}else{
			if(thisValue!=null)
				playerStatus.setUrl(PLAYER_PLAY);
			else
				playerStatus.setUrl(PLAYER_DISABLED);
		}
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
