package org.imogene.notifier.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.imogene.notifier.server.common.NotificationInstance;
import org.imogene.notifier.server.services.NotificationService;



public class EntityNotifServlet extends HttpServlet {
	
	/**	 */
	private static final long serialVersionUID = -7270113886774500937L;
	
	private static final String BAD_PARAMTERS = "Bad request parameters";
	
	private Logger logger = Logger.getLogger("org.imogene.notifier.server");
	
	private NotificationFactory factory;
	
	private NotificationService emailService;
	
	private NotificationService smsService;
	
	/** Servlet creation*/
	public EntityNotifServlet(){
		factory = (NotificationFactory) ApplicationContext.getInstance()
		.getBean("notifFactory");
		emailService = (NotificationService) ApplicationContext.getInstance()
		.getBean("emailService");
		smsService = (NotificationService) ApplicationContext.getInstance()
		.getBean("smsService");
	}

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String type = req.getParameter(Constants.ENTIY_TYPE_PARAM);
		String id = req.getParameter(Constants.ENTITY_ID_PARAM);
		String op = req.getParameter(Constants.ENTITY_OP_PARAM);
		
		OutputStream os = resp.getOutputStream();
		if(validateParameters(type, id, op)){
			Set<NotificationInstance> instances = factory.createNotification(type, id, op);
			System.out.println("Nb. of instances : "+instances.size());
			for(NotificationInstance instance : instances){
				if(Constants.SMSMethod == instance.getMethod())				
					smsService.sendNotification(instance);
				else if(Constants.MailMethod == instance.getMethod())
					emailService.sendNotification(instance);
				else 
					logger.warn("The notification method with the code '"+instance.getMethod()+"' is not implemented.");
			}
		}else{
			resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);			
			os.write(BAD_PARAMTERS.getBytes());			
		}
		os.flush();
		os.close();
	}
	
	
	/**
	 * Validate the parameters.
	 * @param type the card entity type
	 * @param id the card entity id
	 * @param op the card entity operation
	 * @return true if the parameters are valid, not otherwise
	 */
	private boolean validateParameters(String type, String id, String op){
		if(type==null || type.equals(""))
			return false;
		if(id==null || id.equals(""))
			return false;
		if(op==null || op.equals(""))
			return false;
		return true;
	}
	
}
