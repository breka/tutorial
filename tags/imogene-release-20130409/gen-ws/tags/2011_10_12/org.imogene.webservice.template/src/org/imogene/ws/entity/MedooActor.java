package org.imogene.ws.entity;

import java.util.Date;
import java.util.Set;


/**
 * This interface describe a Medoo actor, which is a 
 * Medoo bean that is an application user too.
 * @author Medes-IMPS
 */
public interface MedooActor extends MedooBean {
 
 /** Get the String representation of the bean, described by the MainFields*/
 public String getLogin();
 
 /** Get the assigned password for this user */
 public String getPassword();
 
 /** Set the assigned password for this user */
 public void setPassword(String password);

 /** Get the actor last login date */
 public Date getLastLoginDate();

 /** Set the actor last login date */
 public void setLastLoginDate(Date lastLoginDate);
 
 /** Get the local ISO code used by this actor */
 public String getNotificationLocale();
 
 /** Set the local ISO code used by this actor */
 public void setNotificationLocale(String iso);
 
 /** get the default method used to notify this actor */
 public Integer getDefaultNotificationMethod();
 
 /** Set the default method used to notify this actor */
 public void setDefaultnotificationMethod(Integer method);
 
 /** Is the notification activated for this user */
 public Boolean getBeNotified();
 
 /** Set if the notification is activated for this user */
 public void setBeNotified(Boolean notify);
 
 /** Get the role associated to this user*/
 public Set<MedooRole> getRoles();
 
 /** Set the roles associted to this user */
 public void setRoles(Set<MedooRole> pRoles);
 

}
