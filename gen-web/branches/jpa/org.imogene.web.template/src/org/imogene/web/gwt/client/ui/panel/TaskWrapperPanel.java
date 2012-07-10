package org.imogene.web.gwt.client.ui.panel;

import java.util.List;
import java.util.Vector;

import org.imogene.web.gwt.client.ui.form.DisclosureContainerComposite;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Medes-IMPS
 */
public class TaskWrapperPanel extends RoundedWrapperPanel implements
		ValueChangeHandler<String> {

	private static String DEFAULT_COLOR = "default";

	private List<Widget> forms = new Vector<Widget>();

	/** widgets */
	private VerticalPanel taskList;

	/**
	 * Rounded task composite without command and the default color.
	 * 
	 * @param pTitle
	 *            the task composite title.
	 */
	public TaskWrapperPanel(String pTitle) {
		this(pTitle, DEFAULT_COLOR);
	}

	/**
	 * Rounded task composite without command.
	 * 
	 * @param pTitle
	 *            the task composite title
	 * @param pColor
	 *            the task composite color
	 */
	public TaskWrapperPanel(String pTitle, String pColor) {
		this(pTitle, pColor, null);
	}

	/**
	 * Rounded task composite with the default color
	 * 
	 * @param pTitle
	 *            the task composite title
	 * @param pCommands
	 *            the commands to add
	 */
	public TaskWrapperPanel(String pTitle, Hyperlink[] pCommands) {
		this(pTitle, DEFAULT_COLOR, pCommands);
	}

	/**
	 * Rounded task composite
	 * 
	 * @param pTitle
	 *            the task composite title
	 * @param pColor
	 *            the task composite color
	 * @param pCommands
	 *            the commands to add in the header
	 */
	public TaskWrapperPanel(String pTitle, String pColor, Hyperlink[] pCommands) {
		super(pTitle, pColor, pCommands);
		layout();
		properties();
		behavior();
	}

	/** main layout */
	private void layout() {
		taskList = new VerticalPanel();
		initChild(taskList);
	}

	/** main layout properties */
	private void properties() {
		taskList.setStyleName("imogene-RoundedTaskList");
		taskList.setSpacing(0);
	}

	/** global behavior */
	private void behavior() {
		History.addValueChangeHandler(this);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String[] tokenPath = event.getValue().split("/");
		if (tokenPath.length > 0 && tokenPath[0].equals("sub")) {

		}
	}

	public void addForm(DisclosureContainerComposite form) {
		forms.add(form);
		for (int i = 0; i < taskList.getWidgetCount(); i++) {
			if (taskList.getWidget(i) instanceof DisclosureContainerComposite) {
				((DisclosureContainerComposite) taskList.getWidget(i))
						.forceClose();
			}
		}
		taskList.add(form);
		taskList.setCellWidth(form, "100%");
		taskList.setCellVerticalAlignment(form, VerticalPanel.ALIGN_TOP);
	}

	/**
	 * Counts the number of children (not including the first one which is the
	 * current child)
	 * 
	 * @return
	 */
	public int countForms() {
		return taskList.getWidgetCount() - 1;
	}

	public void removeForm(DisclosureContainerComposite form) {
		taskList.remove(form);
		int last = taskList.getWidgetCount() - 1;
		if (last > -1)
			((DisclosureContainerComposite) taskList.getWidget(last))
					.forceOpen();
	}

}
