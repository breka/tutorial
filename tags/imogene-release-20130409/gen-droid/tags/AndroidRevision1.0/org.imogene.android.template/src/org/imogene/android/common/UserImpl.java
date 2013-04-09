package org.imogene.android.common;

import org.imogene.android.Constants.Keys;
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
		super.addValues(context, values);
		if (!TextUtils.isEmpty(mLogin) && mPassword != null && mPassword.length > 0) {
			values.put(Keys.KEY_LOGIN, mLogin);
			values.put(Keys.KEY_PASSWORD, mPassword);
			values.put(Keys.KEY_ROLES, mRoles);
		}
	}

}
