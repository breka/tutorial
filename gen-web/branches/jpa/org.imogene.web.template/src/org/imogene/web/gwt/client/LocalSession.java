package org.imogene.web.gwt.client;

import java.util.HashSet;
import java.util.Set;

import org.imogene.common.entity.ImogActor;

public class LocalSession {

	private static LocalSession instance = new LocalSession();

	private ImogActor currentUser;

	private Set<Integer> edited = new HashSet<Integer>();

	public static LocalSession get() {
		return instance;
	}

	public void setCurrentUser(ImogActor actor) {
		currentUser = actor;
	}

	public ImogActor getCurrentUser() {
		return currentUser;
	}

	public void addToEdited(Integer code) {
		edited.add(code);
	}

	public void removeFromEdited(Integer code) {
		edited.remove(code);
	}

	public boolean isEditing() {
		return !edited.isEmpty();
	}

	public void clearEdited() {
		edited.clear();
	}
}
