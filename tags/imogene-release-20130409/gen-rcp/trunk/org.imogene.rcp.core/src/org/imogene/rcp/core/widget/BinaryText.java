package org.imogene.rcp.core.widget;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.rcp.core.tools.BinaryHelper;
import org.imogene.rcp.core.wrapper.CoreMessages;
import org.imogene.sync.client.binary.BinaryFile;
import org.imogene.sync.client.binary.BinaryFileManager;
/**
 * This class enables to edit and display a binary field
 * @author MEDES-IMPS
 */
public class BinaryText extends Composite implements SelectionListener {
	
	private Text text;
	
	private Button selectButton;
	
	private Button exportButton;
	
	private Image selectImage;
	
	private Image exportImage;
	
	private File file;
	
	private String fileName;
	
	private String[] extensions;
	
	/**
	 * Construct this composite using the standard style.
	 * @param parent the parent composite
	 */
	public BinaryText(Composite parent){
		this(parent, false);
	}
	
	/**
	 * Let the user specify the style
	 * @param parent the parent composite
	 * @param formStyle true if this composite should use the form style.
	 */
	public BinaryText(Composite parent, boolean formStyle){
		
		super(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		setLayout(layout);
		
		if(formStyle){	
			createFormWidget();
		} else {
			createStandardWidget();
		}		
		
		text.setEditable(false);
		text.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.FILL_HORIZONTAL));
		
		selectImage = ImogPlugin.getImageDescriptor("icons/folder.gif").createImage();
		selectButton.setImage(selectImage);
		selectButton.setToolTipText(CoreMessages.getString("tooltip_bin_select"));
		selectButton.addSelectionListener(this);
		selectButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		
		exportImage = ImogPlugin.getImageDescriptor("icons/save-16x16.png").createImage();
		exportButton.setImage(exportImage);	
		exportButton.setToolTipText(CoreMessages.getString("tooltip_bin_copy"));
		exportButton.addSelectionListener(this);		
		exportButton.setVisible(false);		
		GridData exportButtonButtonData = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		exportButton.setLayoutData(exportButtonButtonData);
		exportButtonButtonData.exclude = true;	
		
	}
	
	/**
	 * Create widgets with the standard style
	 */
	private void createStandardWidget(){
		text = new Text(this, SWT.NONE);
		selectButton = new Button(this, SWT.PUSH);
		exportButton = new Button(this, SWT.PUSH);
	}
	
	/**
	 * Create widgets with the HTMLForm style
	 */
	private void createFormWidget(){
		FormToolkit toolkit = new FormToolkit(Display.getCurrent());
		setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		text = toolkit.createText(this, "");		
		selectButton = toolkit.createButton(this, "", SWT.PUSH | SWT.FLAT);	
		exportButton = toolkit.createButton(this, "", SWT.PUSH | SWT.FLAT);	
		toolkit.paintBordersFor(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {				
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		
		if (e.widget.equals(selectButton)) {
			FileDialog fd = new FileDialog(getShell());
			if (extensions != null)
				fd.setFilterExtensions(extensions);
			String result = fd.open();
			if (result != null) {				
				file = new File(result);
				fileName = fd.getFileName();
				text.setText(fileName);
			}
		}
		
		if(e.widget.equals(exportButton)){
			if (file!=null) {
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
				fd.setFileName(file.getName());
				String path = fd.open();
				if(path!=null){
					BinaryHelper.extractBinary(file.getName(), path);
				}				
			}
		}
		
	}
	
	/**
	 * Get the selected file
	 * @return the selected file
	 */
	public File getFile(){
		return file;
	}
	
	/**
	 * Set the embedded file
	 * @param file the embedded file
	 */
	public void setFile(File file){
		this.file = file;
		this.fileName = file.getName();
	}
	
	/**
	 * Set the embedded file
	 * @param binaryId the embedded file id
	 */
	public void setFile(String binaryId){
		
		BinaryFile binaryFile = BinaryHelper.getBinary(binaryId);
		
		if (binaryFile !=null) {
			String filePath = BinaryFileManager.getInstance().buildFilePath(binaryId, binaryFile.getFileName());			
			try {			
				file = new File(filePath);
				text.setText(binaryFile.getFileName());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void dispose() {
		selectImage.dispose();
		super.dispose();
	}
	
	/**
	 * Set the extensions filter
	 * @param extensions the extensions
	 */
	public void setExtensionFilter(String[] extensions){
		this.extensions = extensions;
	}
		
	/**
	 * Set if this widget is editable or not
	 * @param editable true if the widget is editable
	 */
	public void setEditable(boolean editable){
		
		GridData exportButtonButtonData = (GridData) exportButton.getLayoutData();
		GridData selectButtonButtonData = (GridData) selectButton.getLayoutData();
		
		if(editable){
			exportButtonButtonData.exclude = true;
			exportButton.setVisible(false);
			selectButtonButtonData.exclude = false;		
			selectButton.setVisible(true);			
		}
		else{
			exportButtonButtonData.exclude = false;
			exportButton.setVisible(true);
			selectButtonButtonData.exclude = true;
			selectButton.setVisible(false);		
		}
		this.layout(false);
	}
	
	/**
	 * Set if this widget is readonly or not. If yes, sets
	 * the widget as not editable and applies a light grey background
	 * @param readonly true if the widget is readonly
	 */
	public void setReadonly(boolean readonly) {
		setEditable(!readonly);
		if (readonly) 
			text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		else
			text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}
}
