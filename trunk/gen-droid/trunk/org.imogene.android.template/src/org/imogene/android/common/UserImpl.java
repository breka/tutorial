package org.imogene.android.common;

import org.imogene.android.common.interfaces.User;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

public abstract class UserImpl extends EntityImpl implements User {

	protected String mLogin = null;
	protected byte[] mPassword = null;
	protected String mRoles = null;
	
	public final void setLogin(String login) {
		mLogin = login;
	}
	
	public final String getLogin() {
		return mLogin;
	}
	
	public final void setPassword(byte[] password) {
		mPassword = password;
	}
	
	public final byte[] getPassword() {
		return mPassword;
	}
	
	public final void setRoles(String roles) {
		mRoles = roles;
	}
	
	public final String getRoles() {
		return mRoles;
	}
	
	@Override
	protected void addValues(Context context, ContentValues values) {
		if (!TextUtils.isEmpty(mLogin) && mPassword != null && mPassword.length > 0) {
			values.put(User.Columns.LOGIN, mLogin);
			values.put(User.Columns.PASSWORD, mPassword);
			values.put(User.Columns.ROLES, mRoles);
		}
	}
	
}
