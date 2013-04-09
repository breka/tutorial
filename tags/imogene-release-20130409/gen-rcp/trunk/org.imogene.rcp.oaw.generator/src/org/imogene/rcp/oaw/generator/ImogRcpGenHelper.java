package org.imogene.rcp.oaw.generator;

import java.util.List;

import org.imogene.model.core.CardEntity;
import org.imogene.model.core.Project;
import org.imogene.model.core.Thema;



/**
 */
public class ImogRcpGenHelper
{
	
	/**
	 * Sets the first character of a String to upper case
	 * @param from the string to be converted
	 * @return the string starting with an upper case
	 */
	public static String onlyFirstUpper(String from) {
		String to = new String();
		to = from.substring(0, 1).toUpperCase();
		to = to + from.substring(1).toLowerCase();
		return to;
	}
	

	/**
	 * Compute modulo between two number.
	 * 
	 * @param param1 first number
	 * @param param2 second number
	 * @return modulo
	 */
	public static Long modulo(java.lang.Long param1, java.lang.Long param2)
	{
		Long result = new Long(param1.intValue()%param2.intValue());
		return result;		
	}
	
	
	/**
	 * Gets the name of the first thema that contains the cardentity
	 * @param entity the entity for which the thema is wanted
	 * @param project the project the entity belongs to
	 * @return the name of the first thema that contains the cardentity
	 */
	public static String getFirstThemaName(CardEntity entity, Project project) {
		
		List<Thema> themas = project.getThemas();
		if (themas!=null) {
			
			for(Thema thema:themas) {
				List<CardEntity> themaEntities = thema.getEntities();
				if (themaEntities!=null && themaEntities.contains(entity))
					return thema.getName();			
			}
		}
		return null;
	}
	
	/**
	 * Gets the name of the first thema that contains the cardentity
	 * @param entity the entity for which the thema is wanted
	 * @param project the project the entity belongs to
	 * @return the name of the first thema that contains the cardentity
	 */
	public static Thema getFirstThema(CardEntity entity, Project project) {
		
		List<Thema> themas = project.getThemas();
		if (themas!=null) {
			
			for(Thema thema:themas) {
				List<CardEntity> themaEntities = thema.getEntities();
				if (themaEntities!=null && themaEntities.contains(entity))
					return thema;			
			}
		}
		return null;
	}
	
}
