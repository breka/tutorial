package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.BinaryDesc;
import org.imogene.web.gwt.client.BinaryTools;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.ui.VideoPlayer;
import org.imogene.web.gwt.client.ui.VideoPopupPanel;
import org.imogene.web.gwt.client.ui.field.upload.ImogBinaryUploader;
import org.imogene.web.gwt.remote.UtilServicesAsyncFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Composite to manage the display of Video fields
 * 
 * @author Medes-IMPS
 */
public class ImogVideoField extends ImogFieldAbstract<String> implements
		ClickHandler, ImogUploader {

	private static final String PLAY_URL = GWT.getModuleBaseURL()
			+ "images/video_play_32.png";
	private static final String DISABLE_URL = GWT.getModuleBaseURL()
			+ "images/video_play_32-dis.png";

	/* status - behavior */
	private String thisLabel;
	private String thisValue;
	private boolean edited = false;
	private HandlerRegistration launchHandlerRegistration;

	/* widgets */
	private HorizontalPanel main;
	private Image videoLaunch;

	/* widgets edit */
	private VerticalPanel editPanel;
	private ImogBinaryUploader uploader;

	/* widgets display */
	private HorizontalPanel infoPanel;
	private VerticalPanel downloadPanel;
	private HTML nameLabel;
	private HTML sizeLabel;
	private HTML downloadLink;

	/**
	 */
	public ImogVideoField() {
		layout();
	}

	/**
	 * @param title
	 *            the field label
	 */
	public ImogVideoField(String title) {
		this();
		thisLabel = title;
	}

	/**
	 */
	private void layout() {
		main = new HorizontalPanel();
		videoLaunch = new Image();
		main.add(videoLaunch);

		/* display panel */
		infoPanel = new HorizontalPanel();
		downloadPanel = new VerticalPanel();
		downloadLink = new HTML();
		nameLabel = new HTML(BaseNLS.messages().field_binary_file(
				BaseNLS.constants().binary_nofile()));
		sizeLabel = new HTML(BaseNLS.messages().field_binary_size("-"));
		downloadPanel.add(nameLabel);
		downloadPanel.add(sizeLabel);
		infoPanel.add(downloadPanel);
		infoPanel.add(downloadLink);

		/* edit */
		editPanel = new VerticalPanel();
		uploader = new ImogBinaryUploader();
		editPanel.add(uploader);

		main.add(infoPanel);
		initWidget(main);
		properties();
	}

	/**
	 */
	private void properties() {
		main.setSpacing(3);
		main.setWidth("100%");
		videoLaunch.setUrl(DISABLE_URL);
		main.setCellWidth(videoLaunch, "32px");
		main.setCellVerticalAlignment(videoLaunch, HorizontalPanel.ALIGN_MIDDLE);
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
		if (thisLabel != null)
			return thisLabel;
		return "";
	}

	@Override
	public String getValue() {
		if (uploader.getEntityId() != null)
			return uploader.getEntityId();
		return thisValue;
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
	}

	@Override
	public void setValue(String value) {
		if (value != null && !"".equals(value)) {
			/* download controller */
			setPlayerStatus();
			downloadLink.setHTML(BinaryTools.DOWNLOAD_TMPL.replace(
					"%PARAM_ID%", value));
			if (thisValue != value)
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
		if (!edited && editable) {
			main.remove(infoPanel);
			main.add(editPanel);
		}
		if (edited && !editable) {
			main.remove(editPanel);
			main.add(infoPanel);
		}
		edited = editable;
		setPlayerStatus();
	}

	/**
	 */
	private void setPlayerStatus() {
		if (!edited && thisValue != null) {
			videoLaunch.setUrl(PLAY_URL);
			videoLaunch.setStylePrimaryName("imogene-ImageLink");
			if (launchHandlerRegistration == null)
				launchHandlerRegistration = videoLaunch.addClickHandler(this);
		} else {
			videoLaunch.setUrl(DISABLE_URL);
			videoLaunch.removeStyleName("imogene-ImageLink");
			if (launchHandlerRegistration != null) {
				launchHandlerRegistration.removeHandler();
				launchHandlerRegistration = null;
			}
		}
	}

	@Override
	public boolean isEdited() {
		return edited;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(videoLaunch)) {
			VideoPlayer player = new VideoPlayer("Video diabsat", 300, 400,
					GWT.getHostPageBaseURL() + "getbinary?flvId=" + getValue());
			PopupPanel popup = new VideoPopupPanel(player);
			popup.show();
		}
	}

	@Override
	public boolean isUploading() {
		return uploader.isUploading();
	}

	/**
	 * display the binary meta-data
	 */
	public void setBinaryMetadata(String value) {
		UtilServicesAsyncFacade.getInstance().getBinaryDesc(value,
				new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						BinaryDesc binaryDesc = BinaryTools
								.createBinaryDesc(result);
						nameLabel.setHTML(BaseNLS.messages().field_binary_file(
								binaryDesc.getName()));
						sizeLabel.setHTML(BaseNLS.messages().field_binary_size(
								BinaryTools.getSizeAsString(binaryDesc
										.getSize())));
					}

					@Override
					public void onFailure(Throwable arg0) {
					}
				});
	}
}
