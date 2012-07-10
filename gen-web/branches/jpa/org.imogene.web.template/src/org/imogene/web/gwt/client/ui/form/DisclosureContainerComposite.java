package org.imogene.web.gwt.client.ui.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Medes-IMPS
 */
public class DisclosureContainerComposite extends Composite implements
		OpenHandler<DisclosurePanel>, CloseHandler<DisclosurePanel> {

	/* constants */
	private static final String DEFAULT_COLOR = "default";
	private static final String DEFAULT_ICON = "entity_default_24.png";
	private static final String FORM_COLOR = "form";

	/* status */
	private boolean activated = true;
	private boolean lockedSatus = true;
	private boolean withHeader = true;
	private String color = DEFAULT_COLOR;

	/* widgets */
	private DisclosurePanel discPanel;
	private HorizontalPanel headerPanel;
	private HorizontalPanel buttonsPanel;
	private HorizontalPanel contentPanel;
	private SimplePanel imagePanel;
	private Widget content;
	private HTML headerLabel;
	private Image headerIcon;
	private String title;

	public DisclosureContainerComposite() {
		this.withHeader = false;
		layout();
		properties();
		setActivated(false);
	}

	public DisclosureContainerComposite(String pTitle) {
		this(pTitle, null);
	}

	public DisclosureContainerComposite(String pTitle, String pColor) {
		if (pColor != null)
			color = FORM_COLOR;
		title = pTitle;
		layout();
		properties();
		setActivated(false);
	}

	public void setContent(Widget pContent) {
		if (content != null)
			contentPanel.remove(content);
		content = pContent;
		contentPanel.add(content);
		contentPanel.setCellWidth(content, "100%");
	}

	public void setImage(Image image) {
		if (image != null)
			imagePanel.setWidget(image);
		contentPanel.setCellWidth(imagePanel, "32px");
		contentPanel.setCellHeight(imagePanel, "32px");
	}

	public void setActivated(boolean pActivated) {
		activated = pActivated;
		discPanel.setAnimationEnabled(activated);
		if (!pActivated)
			lockedSatus = discPanel.isOpen();
	}

	public void forceOpen() {
		setActivated(true);
		discPanel.setOpen(true);
		if (withHeader)
			buttonsPanel.setVisible(true);
		setActivated(false);
	}

	public void forceClose() {
		setActivated(true);
		discPanel.setOpen(false);
		if (withHeader)
			buttonsPanel.setVisible(false);
		setActivated(false);
	}

	/** main layout */
	private void layout() {
		discPanel = new DisclosurePanel();
		contentPanel = new HorizontalPanel();
		imagePanel = new SimplePanel();
		contentPanel.add(imagePanel);
		if (withHeader) {
			layoutHeader();
			discPanel.setHeader(headerPanel);
		}
		discPanel.setContent(contentPanel);
		initWidget(discPanel);
	}

	/** main layout properties */
	private void properties() {

		discPanel.setAnimationEnabled(true);
		discPanel.setOpen(true);
		discPanel.addOpenHandler(this);
		discPanel.addCloseHandler(this);

		if (withHeader) {
			headerPanel.setWidth("100%");
			contentPanel.setWidth("99%");
		} else {
			discPanel.setStyleName("imogene-discPanel");
			contentPanel.setHeight("100%");
			contentPanel.setWidth("100%");
		}
		contentPanel.setCellVerticalAlignment(imagePanel,
				HorizontalPanel.ALIGN_MIDDLE);
	}

	/** header part */
	private void layoutHeader() {
		headerPanel = new HorizontalPanel();
		/* icon */
		headerIcon = new Image(GWT.getModuleBaseURL() + "images/"
				+ DEFAULT_ICON);
		/* label */
		headerLabel = new HTML(title);
		/* buttons */
		buttonsPanel = new HorizontalPanel();
		/* header */
		headerPanel.add(headerIcon);
		headerPanel.add(headerLabel);
		headerPanel.add(buttonsPanel);
		layoutProperties();
	}

	private void layoutProperties() {
		headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		/* icon */
		headerIcon.setVisible(false);
		headerPanel.setCellWidth(headerIcon, "16px");
		headerPanel.setCellHeight(headerIcon, "16px");
		headerPanel.setCellVerticalAlignment(headerIcon,
				HasVerticalAlignment.ALIGN_MIDDLE);
		/* label */
		headerLabel.setStylePrimaryName("imogene-DisclosureContainerHeader");
		if (!color.equals(FORM_COLOR))
			headerLabel.addStyleDependentName(color);
		headerPanel.setCellVerticalAlignment(headerLabel,
				HasVerticalAlignment.ALIGN_MIDDLE);
		/* buttons */
		buttonsPanel.setStylePrimaryName("imogene-DisclosureContainerHeader");
		if (!color.equals(FORM_COLOR))
			buttonsPanel.addStyleDependentName(color);
		headerPanel.setCellHorizontalAlignment(buttonsPanel,
				HorizontalPanel.ALIGN_RIGHT);
		headerPanel.setCellVerticalAlignment(buttonsPanel,
				HasVerticalAlignment.ALIGN_MIDDLE);
		/* panel */
		headerPanel.setStylePrimaryName("imogene-DisclosureContainerHeader");
		headerPanel.addStyleDependentName(color);
	}

	@Override
	public void onOpen(OpenEvent<DisclosurePanel> event) {
		if (!activated && !lockedSatus) {
			discPanel.setOpen(false);
		}
	}

	@Override
	public void onClose(CloseEvent<DisclosurePanel> event) {
		if (!activated && lockedSatus) {
			discPanel.setOpen(true);
		}
	}

	/**
	 * Set the panel title
	 */
	public void setTitle(String newTitle) {
		headerLabel.setHTML(newTitle);
	}

	/**
	 * Set the icon.
	 * 
	 * @param iconPath
	 *            the image url
	 */
	public void setIconPath(String iconPath) {
		headerIcon.setUrl(iconPath);
	}

	/**
	 * Set header icon visible
	 * 
	 * @param display
	 *            true, to display the header icon.
	 */
	public void displayHeaderIcon(boolean display) {
		headerIcon.setVisible(display);
	}

	/**
	 * Add buttons to the right part of the header.
	 */
	public void addButton(ButtonBase button) {
		buttonsPanel.add(button);
	}

}
