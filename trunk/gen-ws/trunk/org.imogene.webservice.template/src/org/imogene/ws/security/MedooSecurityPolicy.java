package org.imogene.ws.security;

import java.util.List;

import org.imogene.ws.entity.MedooActor;
import org.imogene.ws.entity.MedooBean;




public interface MedooSecurityPolicy {

	/**
	 * 
	 * @param bean The bean to secure
	 * @param actor The actor of this session
	 * @return a secured bean
	 */
	public MedooBean toSecure(MedooBean bean, MedooActor actor);
	
	/**
	 * 
	 * @param list
	 * @param actor
	 * @return
	 */
	public List<?> toSecure(List<?> list, MedooActor actor);
	
	/**
	 * 
	 * @param bean The secured bean
	 * @param actor The session actor
	 * @return The global 'unsecured' bean
	 */
	public MedooBean toHibernate(MedooBean bean, MedooActor actor);
	
	/**
	 * 
	 * @param list
	 * @param actor
	 * @return
	 */
	public List<?> toHibernate(List<?> list, MedooActor actor);
}
