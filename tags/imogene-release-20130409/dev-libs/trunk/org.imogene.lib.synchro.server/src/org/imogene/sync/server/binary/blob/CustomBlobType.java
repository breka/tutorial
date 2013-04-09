package org.imogene.sync.server.binary.blob;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.orm.hibernate3.support.AbstractLobType;

/**
 * Hibernate workaround to manage Blobs 
 * as Blob in java and as bite[] in Db
 * @author http://sirinsevinc.wordpress.com/tag/postgresql/
 */
public class CustomBlobType extends AbstractLobType {

	// called when the column is to be retrieved from db
	protected Object nullSafeGetInternal(ResultSet rs, String[] names,
			Object owner, LobHandler lobHandler) throws SQLException {

		InputStream is = lobHandler.getBlobAsBinaryStream(rs, names[0]);

		return new BlobStream(is);
	}

	// called when the data is to be stored in db
	protected void nullSafeSetInternal(PreparedStatement ps, int index,
			Object value, LobCreator lobCreator) throws SQLException {
		if (value != null) {
			BlobStream blob = (BlobStream) value;
			lobCreator.setBlobAsBinaryStream(ps, index, blob.getStream(), blob.getLength());

		} else {
			lobCreator.setBlobAsBytes(ps, index, null);
		}
	}

	/**
	 * defines what type of object that this class returns or gets as a a
	 * parameter when the above methods are called.
	 * 
	 * @return Class
	 */
	public Class<?> returnedClass() {
		return BlobStream.class;
	}

	/**
	 * Return the SQL type codes for the columns mapped by this type. The codes
	 * are defined on <tt>java.sql.Types</tt>.
	 * 
	 * @see java.sql.Types
	 * @return int[] the typecodes
	 */
	public int[] sqlTypes() {
		return new int[] { Types.BLOB };
	}
}
