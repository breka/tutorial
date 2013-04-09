package org.imogene.ws.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for the XML serialization of lists of entity LocalizedText
 * @author MEDES-IMPS
 */
@XmlRootElement(name = "localizedtexts")
public class LocalizedTextList {

	private List<LocalizedText> localizedTexts;

	public LocalizedTextList() {
	}

	public LocalizedTextList(List<LocalizedText> localizedTexts) {
		this.localizedTexts = localizedTexts;
	}

	@XmlElement(name = "localizedtext")
	public List<LocalizedText> getLocalizedTexts() {
		return localizedTexts;
	}

	public void setLocalizedTexts(List<LocalizedText> localizedTexts) {
		this.localizedTexts = localizedTexts;
	}
}
