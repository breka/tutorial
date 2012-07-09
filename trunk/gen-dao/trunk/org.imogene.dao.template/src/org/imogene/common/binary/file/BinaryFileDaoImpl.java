package org.imogene.common.binary.file;

import org.imogene.common.dao.ImogBeanDaoImpl;
import org.springframework.transaction.annotation.Transactional;

public class BinaryFileDaoImpl extends ImogBeanDaoImpl<BinaryFile> implements BinaryFileDao {

	protected BinaryFileDaoImpl() {
		super(BinaryFile.class);
	}

	@Override
	@Transactional
	public void delete() {
		em.createQuery("DELETE FROM Binary").executeUpdate();
	}

}
