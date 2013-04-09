package org.imogene.rcp.core.widget;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.ImogPlugin;
import org.imogene.sync.client.BeanKeyGenerator;
import org.imogene.sync.localizedtext.LocalizedText;

public class LocalizedTextCombo extends Composite {
	
	private FormToolkit mToolkit;
	
	private String[] mLocales;
	
	private Combo mLanguageList;
	
	private Text mText;
	
	private Map<String, LocalizedText> mValues = new HashMap<String, LocalizedText>();
	
	private String mValue;
	
	public LocalizedTextCombo(Composite parent, String[] locales) {
		this(parent, locales, false);
	}
	
	public LocalizedTextCombo(Composite parent, String[] locales, boolean isArea) {
		super(parent, SWT.NONE);
		
		mLocales = locales;
		
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		setLayout(layout);
		
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		
		mToolkit = new FormToolkit(parent.getDisplay());
		
		if (isArea) {
			mText = mToolkit.createText(this, "", SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		} else {
			mText = mToolkit.createText(this, "");
		}
		mText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		if (isArea) {
			((GridData) mText.getLayoutData()).heightHint=80;
		}
		mText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String locale = mLanguageList.getItem(mLanguageList.getSelectionIndex());
				LocalizedText text;
				if (mValues.containsKey(locale)) {
					text = mValues.get(locale);
				} else {
					setFieldId();
					text = new LocalizedText();
					text.setId(BeanKeyGenerator.getNewId("LTXT"));
					text.setCreated(new Date(System.currentTimeMillis()));
					text.setCreatedBy(ImogPlugin.getDefault().getCurrentUserIdentity().getLogin());
					text.setFieldId(mValue);
					text.setLocale(locale);
					mValues.put(locale, text);
				}
				text.setModified(new Date(System.currentTimeMillis()));
				text.setModifiedBy(ImogPlugin.getDefault().getCurrentUserIdentity().getLogin());
				text.setModifiedFrom(ImogPlugin.getDefault().getTerminalId());
				text.setValue(mText.getText());
			}
		});
		
		mLanguageList = new Combo(this, SWT.SINGLE | SWT.READ_ONLY);
		mLanguageList.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER));
		mLanguageList.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String locale = mLanguageList.getItem(mLanguageList.getSelectionIndex());
				if (mValues.containsKey(locale)) {
					mText.setText(mValues.get(locale).getValue());
				} else {
					mText.setText("");
				}
				super.widgetSelected(e);
			}
		});
		
		fill();
		
		mToolkit.paintBordersFor(this);
	}
	
	/**
	 * Fill the combo list with the available languages
	 */
	private void fill() {
		mLanguageList.removeAll();
		String current = Locale.getDefault().getLanguage();
		int index = 0;
		for(String locale : mLocales) {
			mLanguageList.add(locale);
			mLanguageList.setItem(index, locale);
			if (current.equals(locale)) {
				mLanguageList.select(index);
			}
			index++;
		}
	}
	
	public void setValue(List<LocalizedText> list) {
		for (LocalizedText text : list) {
			if (mValue == null) {
				mValue = text.getFieldId();
			}
			mValues.put(text.getLocale(), text);
		}
		String locale = Locale.getDefault().getLanguage();
		if (!setLocale(locale)) {
			setFirstAvailable();
		}
	}
	
	private boolean setLocale(String locale) {
		if (mValues.containsKey(locale)) {
			String text = mValues.get(locale).getValue();
			if (text != null && !text.isEmpty()) {
				int count = mLanguageList.getItemCount();
				for (int i = 0; i < count; i++) {
					if (locale.equals(mLanguageList.getItem(i))) {
						mLanguageList.select(i);
						mText.setText(text);
						return true;
					}
				}
				return false;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private void setFirstAvailable() {
		for (LocalizedText text : mValues.values()) {
			if (setLocale(text.getLocale())) {
				return;
			}
		}
		int count = mLanguageList.getItemCount();
		if (count > 0) {
			mLanguageList.select(0);
		}
	}
	
	public List<LocalizedText> getUpdatedLocalizedTexts() {
		return new Vector<LocalizedText>(mValues.values());
	}
	
	public String getValue() {
		return mValue;
	}
	
	/**
	 * Sets a unique id for the field when created
	 */
	private void setFieldId() {				
		if (mValue == null || mValue.equals("")) {
			mValue = BeanKeyGenerator.getNewId("FLD");
		}
	}
	
	public boolean isEmpty() {
		for (LocalizedText text : mValues.values()) {
			if (text.getValue() != null && !text.getValue().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isExpressionValidated(String regex) {
		for (LocalizedText text : mValues.values()) {
			if (text.getValue() == null || !text.getValue().matches(regex))
				return false;
		}
		return true;
	}
	
	public void setEditable(boolean editable) {
		mText.setEditable(editable);
	}
	
	public void setReadOnly(boolean readonly) {
		mText.setEditable(!readonly);
		if (readonly) 
			mText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		else
			mText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}

}
