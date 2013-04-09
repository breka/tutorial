package org.imogene.web.gwt.client.sync;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SynchronizableServiceAsync  {

	public void listAll(AsyncCallback<List<SynchronizableEntity>> callback); 
}
