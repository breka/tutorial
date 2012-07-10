package org.imogene.notif.web.gwt.client;

import java.util.List;

import org.imogene.common.entity.ImogActor;
import org.imogene.notif.web.gwt.remote.RoleActorServiceFacade;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.ui.field.ImogFieldAbstract;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Fields that enables to select severals actor.
 * 
 * @author Medes-IMPS
 */
public class ImogActorsField extends ImogFieldAbstract<String> implements
		ClickHandler {

	private static String SEPARATOR = ";";

	private HorizontalPanel main;
	private VerticalPanel buttons;
	private Image add;
	private Image remove;
	private Image waiting;
	private String label;
	private boolean isEdited;
	private ListBox list;

	public ImogActorsField() {
		layout();
		properties();
		setBehavior();
	}

	private void layout() {
		buttonsLayout();
		main = new HorizontalPanel();
		list = new ListBox();
		main.add(list);
		main.add(buttons);
		initWidget(main);
	}

	private void buttonsLayout() {
		buttons = new VerticalPanel();
		waiting = new Image(GWT.getModuleBaseURL() + "images/wait_actor.gif");
		add = new Image(GWT.getModuleBaseURL() + "images/relation_affect.png");
		add.setTitle(BaseNLS.constants().button_assign());
		remove = new Image(GWT.getModuleBaseURL()
				+ "images/relation_remove.png");
		remove.setTitle(BaseNLS.constants().button_remove());
		buttons.add(waiting);
		buttons.add(add);
		buttons.add(remove);
	}

	private void properties() {
		list.setStylePrimaryName("imogene-FormText");
		list.setVisibleItemCount(5);

		add.setVisible(false);
		remove.setVisible(false);
		waiting.setVisible(false);
	}

	/**
	 * Set the buttons behaviors
	 */
	private void setBehavior() {
		add.addClickHandler(this);
		remove.addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(add)) {
			ActorsSelectionBox box = new ActorsSelectionBox();
			box.show(add.getAbsoluteLeft(), add.getAbsoluteTop());
		}
		if (event.getSource().equals(remove)) {
			int selection = list.getSelectedIndex();
			if (selection > -1) {
				list.removeItem(selection);
			}
		}
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getValue() {
		if (list.getItemCount() < 1)
			return null;
		else {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < list.getItemCount(); i++) {
				buffer.append(list.getValue(i));
				buffer.append(";");
			}
			return buffer.toString();
		}
	}

	@Override
	public boolean isEdited() {
		return isEdited;
	}

	@Override
	public void setEnabled(boolean editable) {
		list.setEnabled(editable);
		add.setVisible(editable);
		remove.setVisible(editable);
	}

	@Override
	public void setLabel(String pLabel) {
		label = pLabel;
	}

	@Override
	public void setValue(String value) {
		list.clear();
		String[] ids = value.split(SEPARATOR);
		waiting.setVisible(true);
		RoleActorServiceFacade.getInstance().listActor(
				new PopulateWithActors(ids));
	}

	@Override
	public void setValue(String value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}

	@Override
	public boolean validate() {
		if (isMandatory() && list.getSelectedIndex() == -1)
			return false;
		return true;
	}

	/**
	 * Set the number of visible item
	 * 
	 * @param count
	 */
	public void setVisibleItem(int count) {
		list.setVisibleItemCount(count);
	}

	/* *************** */
	/* PRIVATE CLASSES */
	/* *************** */

	/**
	 * Fill the list with actor matching the given ids.
	 * 
	 * @author Medes-IMPS
	 */
	private class PopulateWithActors implements AsyncCallback<List<ImogActor>> {

		String[] ids;

		public PopulateWithActors(String[] pIds) {
			ids = pIds;
		}

		@Override
		public void onFailure(Throwable th) {
			GWT.log("Error retrieving the actors", th);
		}

		@Override
		public void onSuccess(List<ImogActor> actors) {
			for (ImogActor actor : actors) {
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].equals(actor.getId()))
						list.addItem(actor.getLogin(), actor.getId());
				}
			}
			waiting.setVisible(false);
		}

	}

	/**
	 * Displays a popup panel to select the actor to add.
	 * 
	 * @author Medes-IMPS
	 */
	private class ActorsSelectionBox extends PopupPanel implements ClickHandler {

		private VerticalPanel panel;
		private ListBox listActors;
		private HorizontalPanel buttons;
		private Button ok;
		private Button cancel;
		private HTML title;

		public ActorsSelectionBox() {
			super(true);
			layout();
			properties();
			setBehavior();
		}

		private void layout() {
			panel = new VerticalPanel();
			title = new HTML("Select actors to add");
			panel.add(title);
			listActors = new ListBox(true);
			panel.add(listActors);
			layoutButtons();
			panel.add(buttons);
			setWidget(panel);
		}

		public void properties() {
			setStylePrimaryName("imogene-RelationSelectionBox");
			addStyleDependentName("notif");

			panel.setSize("100%", "100%");

			title.setStylePrimaryName("imogene-RelationSelectionBox-title");
			title.addStyleDependentName("notif");

			listActors.setVisibleItemCount(6);
			listActors
					.setStylePrimaryName("imogene-RelationSelectionBox-Listbox");

			buttons.setStylePrimaryName("imogene-RelationSelectionBox-ButtonsPanel");

			ok.setStylePrimaryName("imogene-Button");
			cancel.setStylePrimaryName("imogene-Button");
		}

		private void layoutButtons() {
			buttons = new HorizontalPanel();
			ok = new Button("ok");
			cancel = new Button("cancel");
			buttons.add(ok);
			buttons.add(cancel);
		}

		private void setBehavior() {
			ok.addClickHandler(this);
			cancel.addClickHandler(this);
		}

		@Override
		public void onClick(ClickEvent event) {
			if (event.getSource().equals(ok)) {
				for (int i = 0; i < listActors.getItemCount(); i++) {
					if (listActors.isItemSelected(i)) {
						list.addItem(listActors.getItemText(i),
								listActors.getValue(i));
					}
				}
				hide();
			}
			if (event.getSource().equals(cancel)) {
				hide();
			}
		}

		/**
		 * Show the popup panel panel at the given position
		 * 
		 * @param left
		 *            the absolute left
		 * @param top
		 *            the absolute top
		 */
		public void show(int left, int top) {
			setPopupPosition(left, top);
			show();
			RoleActorServiceFacade.getInstance().listActor(
					new AsyncCallback<List<ImogActor>>() {
						@Override
						public void onFailure(Throwable arg0) {
						}

						@Override
						public void onSuccess(List<ImogActor> result) {
							for (ImogActor a : result) {
								if (!isPresent(a))
									listActors.addItem(
											a.getLogin() + " (" + a.getId()
													+ ")", a.getId());
							}
						}
					});
		}

		/**
		 * Filter the actor already present
		 * 
		 * @param actor
		 *            the actor to test
		 * @return true is present, false otherwise
		 */
		private boolean isPresent(ImogActor actor) {
			for (int i = 0; i < list.getItemCount(); i++) {
				if (list.getValue(i).equals(actor.getId()))
					return true;
			}
			return false;
		}
	}

}
