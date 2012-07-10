package org.imogene.web.gwt.client.ui.field.upload;

import gwtupload.client.BaseUploadStatus;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploadStatus.UploadCancelHandler;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStartUploaderHandler;
import gwtupload.client.Uploader;

import org.imogene.web.gwt.client.i18n.BaseNLS;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

/**
 * Imogene binary uploader widget. this class uses the GWTupload projet.
 * 
 * @author Medes-IMPS
 */

public class ImogBinaryUploader extends Composite implements
		UploadCancelHandler, OnFinishUploaderHandler, OnStartUploaderHandler {

	private VerticalPanel main;
	private Uploader embedded;
	private IUploadStatus statusBar;
	private String entityId = null;
	private Label label;
	private boolean uploading = false;

	public ImogBinaryUploader() {
		main = new VerticalPanel();
		createInstance();
		label = new Label(BaseNLS.constants().binary_select());
		main.add(label);
		main.add(embedded);
		initWidget(main);
		properties();
	}

	private void properties() {
		embedded.setFileInputSize(20);
		embedded.setAutoSubmit(true);
	}

	/**
	 * Set the uploader title label
	 * 
	 * @param title
	 *            the title label
	 */
	public void setLabel(String title) {
		label.setText(title);
	}

	/**
	 * Get the id of the binary entity associated with the last binary file.
	 * 
	 * @return the entity id
	 */
	public String getEntityId() {
		return entityId;
	}

	@Override
	public void onCancel() {
		entityId = null;
		uploading = false;
		if (embedded.getStatus().equals(Status.SUCCESS)) {
			setNew();
		}
	}

	@Override
	public void onStart(IUploader uploader) {
		uploading = true;
	}

	@Override
	public void onFinish(IUploader uploader) {
		uploading = false;
		if (uploader.getStatus().equals(Status.SUCCESS)) {
			Document doc = XMLParser.parse(embedded.getServerResponse());
			if (doc.getElementsByTagName("entityid").getLength() > 0) {
				Node tag = doc.getElementsByTagName("entityid").item(0);
				if (tag.getChildNodes().getLength() > 0) {
					entityId = tag.getChildNodes().item(0).getNodeValue();
				}
			}
		}
		if (uploader.getStatus().equals(Status.CANCELED)) {
			setNew();
		}
	}

	/**
	 * reset the uploader
	 */
	public void setNew() {
		main.remove(embedded);
		createInstance();
		main.add(embedded);
		properties();
	}

	/**
	 * @return true if the uploader is uploading, false otherwise.
	 */
	public boolean isUploading() {
		return uploading;
	}

	/**
	 * Create new instance of this uploader
	 */
	private void createInstance() {
		embedded = null;
		statusBar = null;
		embedded = new Uploader();
		embedded.addOnFinishUploadHandler(this);
		embedded.addOnStartUploadHandler(this);
		statusBar = new BaseUploadStatus();
		statusBar.addCancelHandler(this);
		embedded.setStatusWidget(statusBar);
	}
}
