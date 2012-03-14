package org.imogene.sync.server.clientfilter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.sync.serializer.ImogSerializationException;
import org.imogene.sync.serializer.stream.EntityStreamSerializer;
import org.imogene.uao.clientfilter.ClientFilter;

/**
 * Serialize and un-serialize a ClientFilter
 * @author MEDES-IMPS
 */
public class ClientFilterStreamSerializer implements EntityStreamSerializer {

	private Logger logger = Logger
			.getLogger("org.imogene.clientfilter.serializer.stream");

	@SuppressWarnings("unused")
	private DataHandlerManager dataManager;

	/*
	 * (non-Javadoc)
	 * @see EntityStreamSerializer#deSerialize(java.io.DataInputStream)
	 */
	public Synchronizable deSerialize(DataInputStream data)
			throws ImogSerializationException {
		logger.debug("Un-serializing a ClientFilter");
		ClientFilter entity = null;
		try {
			entity = new ClientFilter();
			entity.setId(data.readUTF());
			entity.setModifiedBy(data.readUTF());
			entity.setModifiedFrom(data.readUTF());
			entity.setModified(new Date(data.readLong()));
			entity.setUploadDate(new Date(data.readLong()));
			entity.setCreatedBy(data.readUTF());
			entity.setCreated(new Date(data.readLong()));

			/* UserId */
			String userId = data.readUTF();
			if (!userId.equals(""))
				entity.setUserId(userId);

			/* TerminalId */
			String terminalId = data.readUTF();
			if (!terminalId.equals(""))
				entity.setTerminalId(terminalId);

			/* CardEntity */
			String cardEntity = data.readUTF();
			if (!cardEntity.equals(""))
				entity.setCardEntity(cardEntity);

			/* EntityField */
			String entityField = data.readUTF();
			if (!entityField.equals(""))
				entity.setEntityField(entityField);

			/* Operator */
			String operator = data.readUTF();
			if (!operator.equals(""))
				entity.setOperator(operator);

			/* FieldValue */
			String fieldValue = data.readUTF();
			if (!fieldValue.equals(""))
				entity.setFieldValue(fieldValue);
			
			/* IsNew */
			String isNew = data.readUTF();
			if (!isNew.equals("")) {
				entity.setIsNew(new Boolean(isNew));
			}

			logger.debug("ClientFilter with id " + entity.getId()
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
		logger.debug("Serializing a ClientFilter");
		try {
			ClientFilter clientFilter = (ClientFilter) entity;
			data.writeUTF(clientFilter.getId());
			data.writeUTF(clientFilter.getModifiedBy());
			data.writeUTF(clientFilter.getModifiedFrom());
			data.writeLong(clientFilter.getModified().getTime());
			data.writeLong(clientFilter.getUploadDate().getTime());
			data.writeUTF(clientFilter.getCreatedBy());
			data.writeLong(clientFilter.getCreated().getTime());

			/* UserId */
			if (clientFilter.getUserId() != null)
				data.writeUTF(clientFilter.getUserId());
			else
				data.writeUTF("");

			/* TerminalId */
			if (clientFilter.getTerminalId() != null)
				data.writeUTF(clientFilter.getTerminalId());
			else
				data.writeUTF("");

			/* CardEntity */
			if (clientFilter.getCardEntity() != null)
				data.writeUTF(clientFilter.getCardEntity());
			else
				data.writeUTF("");

			/* EntityField */
			if (clientFilter.getEntityField() != null)
				data.writeUTF(clientFilter.getEntityField());
			else
				data.writeUTF("");

			/* Operator */
			if (clientFilter.getOperator() != null)
				data.writeUTF(clientFilter.getOperator());
			else
				data.writeUTF("");

			/* FieldValue */
			if (clientFilter.getFieldValue() != null)
				data.writeUTF(clientFilter.getFieldValue());
			else
				data.writeUTF("");
			
			/* IsNew */
			if (clientFilter.getIsNew() != null)
				data.writeUTF(String.valueOf(clientFilter.getIsNew()));
			else
				data.writeUTF("");

			logger.debug("ClientFilter with id " + clientFilter.getId()
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
