package org.imogene.common.user;

import org.imogene.common.dao.ImogActorDaoImpl;

public class DefaultUserDaoImpl extends ImogActorDaoImpl<DefaultUser> implements DefaultUserDao {

	protected DefaultUserDaoImpl() {
		super(DefaultUser.class);
	}

	@Override
	public void delete() {
		em.createQuery("DELETE FROM DefaultUser").executeUpdate();
	}

}
