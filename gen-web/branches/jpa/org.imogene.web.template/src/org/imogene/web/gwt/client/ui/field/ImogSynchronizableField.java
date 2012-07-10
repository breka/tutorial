package org.imogene.web.gwt.client.ui.field;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.imogene.common.entity.ShortNameHelper;
import org.imogene.common.sync.entity.SynchronizableEntity;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.sync.SynchronizableServiceFacade;

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

public class ImogSynchronizableField extends
		ImogFieldAbstract<Set<SynchronizableEntity>> implements ClickHandler {

	private String label;
	private boolean isEdited = false;
	private Map<String, SynchronizableEntity> values = new HashMap<String, SynchronizableEntity>();

	private HorizontalPanel panel;
	private ListBox list;
	private VerticalPanel buttons;
	private Image add;
	private Image remove;
	private ShortNameHelper shortNames;

	public ImogSynchronizableField(ShortNameHelper shortNamesHelper) {
		shortNames = shortNamesHelper;
		layout();
		properties();
		setBehavior();
	}

	private void layout() {
		panel = new HorizontalPanel();
		list = new ListBox(true);
		panel.add(list);
		buttons = new VerticalPanel();
		add = new Image(GWT.getModuleBaseURL() + "images/relation_affect.png");
		add.setTitle(BaseNLS.constants().button_assign());
		remove = new Image(GWT.getModuleBaseURL()
				+ "images/relation_remove.png");
		remove.setTitle(BaseNLS.constants().button_remove());
		buttons.add(add);
		buttons.add(remove);
		panel.add(buttons);
		initWidget(panel);
	}

	private void properties() {
		list.setVisibleItemCount(5);
		list.setStylePrimaryName("imogene-FormText");

		add.setVisible(false);
		remove.setVisible(false);
	}

	private void setBehavior() {
		add.addClickHandler(this);
		remove.addClickHandler(this);
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Set<SynchronizableEntity> getValue() {
		Set<SynchronizableEntity> result = new HashSet<SynchronizableEntity>();
		for (SynchronizableEntity entity : values.values()) {
			result.add(entity);
		}
		return result;
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
	public void setValue(Set<SynchronizableEntity> pValue) {
		values = new HashMap<String, SynchronizableEntity>();
		list.clear();
		for (SynchronizableEntity b : pValue) {
			values.put(b.getId(), b);
			list.addItem(getEntityLabel(b), b.getId());
		}
	}

	@Override
	public void setValue(Set<SynchronizableEntity> value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(add)) {
			SynchronizableSelectionBox box = new SynchronizableSelectionBox(
					shortNames);
			box.show(add.getAbsoluteLeft(), add.getAbsoluteTop());
		}
		if (event.getSource().equals(remove)) {
			for (int i = 0; i < list.getItemCount(); i++) {
				if (list.isItemSelected(i)) {
					String toRemove = list.getValue(i);
					values.remove(toRemove);
					list.removeItem(i);
				}
			}
		}
	}

	private String getEntityLabel(SynchronizableEntity se) {
		String label = se.getName();
		String[] choices = label.split("\\.");
		return choices[choices.length - 1];
	}

	/* *************** */
	/* PRIVATE CLASSES */
	/* *************** */

	/**
	 * Displays a popup panel to select the actor to add.
	 * 
	 * @author Medes-IMPS
	 */
	private class SynchronizableSelectionBox extends PopupPanel implements
			ClickHandler {

		private Map<String, SynchronizableEntity> alls = new HashMap<String, SynchronizableEntity>();

		private VerticalPanel panel;
		private ListBox listSyncs;
		private HorizontalPanel buttons;
		private Button ok;
		private Button cancel;
		private HTML title;

		public SynchronizableSelectionBox(ShortNameHelper sNameHelper) {
			super(true);
			layout();
			properties();
			setBehavior();
		}

		private void layout() {
			panel = new VerticalPanel();
			title = new HTML("Select entities to add");
			panel.add(title);
			listSyncs = new ListBox(true);
			panel.add(listSyncs);
			layoutButtons();
			panel.add(buttons);
			setWidget(panel);
		}

		public void properties() {
			setStylePrimaryName("imogene-RelationSelectionBox");
			addStyleDependentName("synchro");

			panel.setSize("100%", "100%");

			title.setStylePrimaryName("imogene-RelationSelectionBox-title");
			title.addStyleDependentName("synchro");

			listSyncs.setVisibleItemCount(6);
			listSyncs
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
				for (int i = 0; i < listSyncs.getItemCount(); i++) {
					if (listSyncs.isItemSelected(i)) {
						list.addItem(listSyncs.getItemText(i),
								listSyncs.getValue(i));
						values.put(listSyncs.getValue(i),
								alls.get(listSyncs.getValue(i)));
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
			SynchronizableServiceFacade.getInstance().listAll(
					new AsyncCallback<List<SynchronizableEntity>>() {
						@Override
						public void onFailure(Throwable arg0) {
						}

						@Override
						public void onSuccess(List<SynchronizableEntity> result) {
							for (SynchronizableEntity se : result) {
								if (!isPresent(se)) {
									alls.put(se.getId(), se);
									listSyncs.addItem(getEntityLabel(se),
											se.getId());
								}
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
		private boolean isPresent(SynchronizableEntity se) {
			for (int i = 0; i < list.getItemCount(); i++) {
				if (list.getValue(i).equals(se.getId()))
					return true;
			}
			return false;
		}
	}

}
