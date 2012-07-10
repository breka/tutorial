package org.imogene.web.gwt.remote.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class EmptyCallback<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable th) {
		// Window.alert("Error during the server connection : " +
		// th.getLocalizedMessage());
	}

	@Override
	public void onSuccess(T arg0) {

	}

}
