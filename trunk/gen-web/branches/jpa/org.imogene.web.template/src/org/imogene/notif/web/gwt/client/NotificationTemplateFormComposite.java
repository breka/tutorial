package org.imogene.notif.web.gwt.client;

import org.imogene.notif.web.gwt.remote.NotificationServiceFacade;
import org.imogene.web.gwt.client.ui.MessageManager;
import org.imogene.web.gwt.client.ui.field.FieldValueChangeHandler;
import org.imogene.web.gwt.client.ui.field.ImogEnumField;
import org.imogene.web.gwt.client.ui.field.ImogField;
import org.imogene.web.gwt.client.ui.field.ImogListBox;
import org.imogene.web.gwt.client.ui.field.ImogTextArea;
import org.imogene.web.gwt.client.ui.field.ImogTextBox;
import org.imogene.web.gwt.client.ui.form.AbstractFormComposite;
import org.imogene.web.gwt.client.ui.form.GroupField;
import org.imogene.web.gwt.common.entity.ShortNameHelper;
import org.imogene.web.gwt.common.id.ImogKeyGenerator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;


/**
 *
 */
public class NotificationTemplateFormComposite extends AbstractFormComposite
		implements
			FieldValueChangeHandler {

	/** status */
	private ShortNameHelper sHelper;
	
	private NotificationTemplate entity;

	private String newEntityId;

	private int uploadMsgId = -1;

	/** notification section widgets */
	private GroupField descriptionSection;
	private ImogTextBox name;	
	private ImogTextBox title;
	private ImogEnumField methods;
	private ImogListBox sourceCard;
	private ImogListBox operations;
	private ImogTextArea message;
	
	/** notification recipient */
	private GroupField recipientSection;	
	private ImogActorsField actors;
	
	

	/**
	 * create a biology form form the 
	 * creation of a new instance.
	 */
	public NotificationTemplateFormComposite(ShortNameHelper helper) {
		super("New notification template", "notification");
		newEntityId = ImogKeyGenerator.generateKeyId("NOTIF");
		sHelper = helper;
		isNew = true;
		layout();
		properties();
		setEditable(true);		
		setRelationButtonBehavior();
		computeVisibility(null, true);
	}

	/**
	 * Create a biology form to display a boat entity.
	 * @param entityId the boat id
	 */
	public NotificationTemplateFormComposite(ShortNameHelper helper, String entityId) {
		this(helper, entityId, false);
	}

	/** 
	 * Create a biology form to display 
	 * or edit an existing boat.
	 * @param entityID the biology id
	 * @param editable true if in edit mode.
	 */
	public NotificationTemplateFormComposite(ShortNameHelper helper, String entityId, boolean editable) {
		super("Loading notification ... please, wait", "notification");
		sHelper = helper;
		layout();
		properties();
		setEditable(editable);
		NotificationServiceFacade.getInstance().getNotification(entityId,
				new PopulateWithNotification());
		setRelationButtonBehavior();
	}

	/**
	 * Layout the form
	 */
	private void layout() {
		layoutDescriptionSection();
		addSection(descriptionSection);		
		layoutRecipientSection();
		addSection(recipientSection);
	}

	/**
	 * Set the layout properties
	 */
	private void properties() {
		setIconPath(GWT.getModuleBaseURL()+"images/notification.png");
		propertiesDescriptionSection();
	}

	/** Notification description section */
	private void layoutDescriptionSection() {
		descriptionSection = new GroupField("Description");
		descriptionSection.setImage(new Image(GWT.getModuleBaseURL()
				+ "/images/notif_desc.png"));		

		/* name */
		name = new ImogTextBox();
		name.setLabel("Name");		
		name.setMandatory(true);
		descriptionSection.addField(name);	
		
		/* methods */
		methods = new ImogEnumField(false, "Method");
		methods.addItem("SMS", "0");
		methods.addItem("Email", "1");	
		descriptionSection.addField(methods);	
		
		/* title */
		title = new ImogTextBox();
		title.setLabel("Title");
		title.setMandatory(true);
		descriptionSection.addField(title);
		
		/* source card */
		sourceCard = new ImogListBox();
		sourceCard.setLabel("Form");
		sourceCard.setMandatory(true);
		descriptionSection.addField(sourceCard);
		popuplateSourceCard();
		
		/* operations */
		operations = new ImogListBox();
		operations.setLabel("Operation");
		operations.setMandatory(true);
		descriptionSection.addField(operations);
		populateOperations();
		
		/* message */
		message = new ImogTextArea();
		message.setLabel("Message");
		message.setMandatory(true);
		descriptionSection.addField(message);
	}
	
	
	/**
	 * Layout the recipient section
	 */
	private void layoutRecipientSection() {
		recipientSection = new GroupField("Recipient");
		recipientSection.setImage(new Image(GWT.getModuleBaseURL()
				+ "/images/notif_users.png"));
		
		actors = new ImogActorsField();
		actors.setVisibleItem(5);
		actors.setLabel("Actors");
		recipientSection.addField(actors);
	}

	
	/**
	 * biologyResults section layout properties
	 */
	private void propertiesDescriptionSection() {
	}

	
	/**
	 * set the behavior of the relation field
	 * embedded button.
	 */
	private void setRelationButtonBehavior() {
	}

	/**
	 * Populate the source card list.
	 */
	private void popuplateSourceCard(){
		if(sHelper!=null){
			for(String key : sHelper.getAllShortNames()){
				sourceCard.addItem(sHelper.getLabelName(key), key);
			}
		}
	}
	
	/**
	 * Populate the operations list bix.
	 */
	private void populateOperations(){
		operations.addItem("Create", "create");
		operations.addItem("Modify", "modify");
		operations.addItem("Delete", "delete");
		operations.addItem("Update", "update");		
	}
	
	/**
	 * Set the editable mode.
	 * @param editable true to edit this form.
	 */
	public void setEditable(boolean editable) {
		super.setEditable(editable);		
		title.setEnabled(editable);
		name.setEnabled(editable);
		methods.setEnabled(editable);
		message.setEnabled(editable);
		sourceCard.setEnabled(editable);
		operations.setEnabled(editable);
		actors.setEnabled(editable);
	}

	/**
	 * Set the notification to display.
	 * @param notif the notification to display
	 */
	private void setNotification(NotificationTemplate notif) {
		setTitle("Notification : " + notif.getName());
		title.setValue(notif.getName());
		name.setValue(notif.getName());
		message.setValue(notif.getMessage());
		operations.setValue(notif.getOperation());
		sourceCard.setValue(notif.getSourceCard());
		actors.setValue(notif.getUserRecipients());
		if(notif.getMethod()!=null)
			methods.setValue(Integer.toString(notif.getMethod().intValue()));		
	}

	/**
	 * Called when retrieving a Biology entity
	 * @author Medes-IMPS	 
	 */
	private class PopulateWithNotification implements AsyncCallback<NotificationTemplate> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("An error occured during " +
					"the connexion to the server.");
		}

		@Override
		public void onSuccess(NotificationTemplate result) {
			entity = result;
			setNotification(entity);			
		}
	}

	
	/**
	 *Call back when no result is expected
	 * @author Medes-IMPS	 
	 */
	private class NoResultCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("An error occured during " +
					"the connexion to the server.");
		}

		@Override
		public void onSuccess(Void result) {
			//setNotification(entity);
			//setEditable(false);
			closeForm();
		}
	}	

	

	/*
	 * (non-Javadoc)
	 * @see org.imogene.gwt.laboudeuse.client.AbstractFormComposite#save()
	 */
	@Override
	protected void save() {
		if (validate()) {
			MessageManager.get().newWarningMessage("Saving the form");
			if (isUploading()) {
				if (uploadMsgId == -1)
					uploadMsgId = MessageManager.get().newWarningMessage(
							"Uploading the attached files");
				Timer timer = new Timer() {

					@Override
					public void run() {
						save();
					}
				};
				timer.schedule(2000);
				return;
			}
			if (entity == null) {
				entity = new NotificationTemplate();
				entity.setId(newEntityId);				
			}			
			entity.setTitle(title.getValue());
			entity.setName(name.getValue());
			entity.setMessage(message.getValue());
			entity.setOperation(operations.getValue());
			entity.setUserRecipients(actors.getValue());
			entity.setSourceCard(sourceCard.getValue());
			if(methods.getValue()!=null)
				entity.setMethod(Integer.decode(methods.getValue()));
			
			/* save operation */
			NotificationServiceFacade.getInstance().saveOrUpdate(entity, isNew,
					new NoResultCallback());
			MessageManager.get().newInfoMessage("Notification saved", 5);
		} else {
			MessageManager.get().newWarningMessage(
					"The notification was not correctly validated, please check it.");
		}
	}

	/**
	 * Is the form uploading binaries
	 * @return true if an upload is in progress
	 */
	private boolean isUploading() {
		boolean result = false;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.gwt.laboudeuse.client.AbstractFormComposite#cancel()
	 */
	@Override
	protected void cancel() {
		setNotification(entity);
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.web.gwt.client.ui.field.FieldValueChangeHandler#onFieldValueChange(org.imogene.web.gwt.client.ui.field.ImogField)
	 */
	@Override
	public void onFieldValueChange(ImogField<?> source) {
		computeVisibility(source, false);
	}

	
	/**
	 * Validate the form
	 * @return true if the form is valid
	 */
	private boolean validate() {
		if (!title.validate())
			return false;
		if (!name.validate())
			return false;	
		return true;
	}

	
	/**
	 * Compute the field visibility
	 */
	private void computeVisibility(ImogField<?> source, boolean allValidation) {

	}

	@Override
	protected void returnToList() {
		History.newItem("list/notif");
	}	
	
	

}
