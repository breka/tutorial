package org.imogene.sync.server.localizedtext;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.sync.localizedtext.LocalizedText;
import org.imogene.sync.serializer.ImogSerializationException;
import org.imogene.sync.serializer.stream.EntityStreamSerializer;

/**
 * Serialize and un-serialize a LocalizedText
 * @author Medes-IMPS
 */
public class LocalizedTextStreamSerializer implements EntityStreamSerializer {

	private Logger logger = Logger
			.getLogger("org.imogene.translatable.serializer.stream");

	private DataHandlerManager dataManager;

	/*
	 * (non-Javadoc)
	 * @see EntityStreamSerializer#deSerialize(java.io.DataInputStream)
	 */
	public Synchronizable deSerialize(DataInputStream data)
			throws ImogSerializationException {
		logger.debug("Un-serializing a LocalizedText");
		LocalizedText entity = null;
		try {
			entity = new LocalizedText();
			entity.setId(data.readUTF());
			entity.setModifiedBy(data.readUTF());
			entity.setModifiedFrom(data.readUTF());
			entity.setModified(new Date(data.readLong()));
			entity.setUploadDate(new Date(data.readLong()));
			entity.setCreatedBy(data.readUTF());
			entity.setCreated(new Date(data.readLong()));

			/* FieldId */
			String fieldId = data.readUTF();
			if (!fieldId.equals(""))
				entity.setFieldId(fieldId);

			/* Locale */
			String locale = data.readUTF();
			if (!locale.equals(""))
				entity.setLocale(locale);

			/* Value */
			String value = data.readUTF();
			if (!value.equals(""))
				entity.setValue(value);

			logger.debug("LocalizedText with id " + entity.getId()
					+ " un-serialized");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw new ImogSerializationException(ex);
		}
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * @see EntityStreamSerializer#serialize(org.imogene.data.Synchronizable, java.io.DataOutputStream)
	 */
	public void serialize(Synchronizable entity, DataOutputStream data)
			throws ImogSerializationException {
		logger.debug("Serializing a LocalizedText");
		try {
			LocalizedText localizedText = (LocalizedText) entity;
			data.writeUTF(localizedText.getId());
			data.writeUTF(localizedText.getModifiedBy());
			data.writeUTF(localizedText.getModifiedFrom());
			data.writeLong(localizedText.getModified().getTime());
			data.writeLong(localizedText.getUploadDate().getTime());
			data.writeUTF(localizedText.getCreatedBy());
			data.writeLong(localizedText.getCreated().getTime());

			/* FieldId */
			if (localizedText.getFieldId() != null)
				data.writeUTF(localizedText.getFieldId());
			else
				data.writeUTF("");

			/* Locale */
			if (localizedText.getLocale() != null)
				data.writeUTF(localizedText.getLocale());
			else
				data.writeUTF("");

			/* Value */
			if (localizedText.getValue() != null)
				data.writeUTF(localizedText.getValue());
			else
				data.writeUTF("");

			logger.debug("LocalizedText with id " + localizedText.getId()
					+ "serialized.");
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw new ImogSerializationException(ex);
		}
	}

	/**
	 * Setter for bean injection
	 * @param manager
	 */
	public void setDataHandlerManager(DataHandlerManager manager) {
		dataManager = manager;
	}
}
