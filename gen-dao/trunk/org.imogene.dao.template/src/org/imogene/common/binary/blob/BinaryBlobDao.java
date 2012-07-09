package org.imogene.common.binary.blob;

import org.imogene.common.binary.BinaryDao;
import org.imogene.common.dao.ImogBeanDaoImpl;

/**
 * Implements a Hibernate DAO for the Binary
 * 
 * @author Medes-IMPS
 */
public class BinaryBlobDao extends ImogBeanDaoImpl<BinaryBlob> implements BinaryDao<BinaryBlob> {

	protected BinaryBlobDao() {
		super(BinaryBlob.class);
	}

	@Override
	public void delete() {
		em.createQuery("DELETE FROM BinaryBlob").executeUpdate();
	}

}
