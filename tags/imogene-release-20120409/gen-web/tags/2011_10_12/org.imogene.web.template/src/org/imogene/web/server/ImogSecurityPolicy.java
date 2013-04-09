package org.imogene.web.server;

import java.util.List;

import org.imogene.web.gwt.common.entity.ImogActor;
import org.imogene.web.gwt.common.entity.ImogBean;




public interface ImogSecurityPolicy {

	/**
	 * 
	 * @param bean The bean to secure
	 * @param actor The actor of this session
	 * @return a secured bean
	 */
	public ImogBean toSecure(ImogBean bean, ImogActor actor);
	
	/**
	 * 
	 * @param list
	 * @param actor
	 * @return
	 */
	public List<?> toSecure(List<?> list, ImogActor actor);
	
	/**
	 * 
	 * @param bean The secured bean
	 * @param actor The session actor
	 * @return The global 'unsecured' bean
	 */
	public ImogBean toHibernate(ImogBean bean, ImogActor actor);
	
	/**
	 * 
	 * @param list
	 * @param actor
	 * @return
	 */
	public List<?> toHibernate(List<?> list, ImogActor actor);
}
