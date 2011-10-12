package org.imogene.notif.web.gwt.remote;

import java.util.List;

import org.imogene.web.gwt.client.Constants;
import org.imogene.web.gwt.common.entity.ImogActor;
import org.imogene.web.gwt.common.entity.ImogRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;


public class RoleActorServiceFacade {

	private static RoleActorServiceFacade instance;

	private RoleActorServiceAsync proxy;

	/**
	 * Private constructor
	 */
	private RoleActorServiceFacade() {
		proxy = (RoleActorServiceAsync) GWT.create(RoleActorService.class);

		ServiceDefTarget target = (ServiceDefTarget) proxy;		
		target.setServiceEntryPoint(GWT.getHostPageBaseURL() + "/roleactor.serv");
	}

	/**
	 * Instance accessor
	 */
	public static RoleActorServiceFacade getInstance() {
		if (instance == null) {
			instance = new RoleActorServiceFacade();
		}
		return instance;
	}

	/* actor services */

	/**
	 * Load the actor with the specified id
	 *@param entityId the actor id
	 *@param callback the result call back
	 */
	public void getActor(String actorId, AsyncCallback<ImogActor> callback) {
		proxy.getActor(actorId, callback);
	}

	/**
	 * List actors	 
	 *@param callback the result call back
	 */
	public void listActor(AsyncCallback<List<ImogActor>> callback) {
		proxy.listActors(callback);
	}

	/* role services */

	/**
	 * Load the actor with the specified id
	 *@param entityId the actor id
	 *@param callback the result call back
	 */
	public void getRole(String id, AsyncCallback<ImogRole> callback) {
		proxy.getRole(id, callback);
	}

	/**
	 * List actors	 
	 *@param callback the result call back
	 */
	public void listRole(AsyncCallback<List<ImogRole>> callback) {
		proxy.listRoles(callback);
	}
	

}
