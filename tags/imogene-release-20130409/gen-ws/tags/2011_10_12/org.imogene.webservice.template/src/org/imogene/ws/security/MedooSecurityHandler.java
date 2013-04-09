package org.imogene.ws.security;



public class MedooSecurityHandler {
	
	private MedooSecurityPolicy policy;
	
	/* The unique instance of this object */
	private static MedooSecurityHandler instance = null;
		
	public static MedooSecurityHandler getInstance(){
		if(instance == null)
			instance = new MedooSecurityHandler();
		return instance;
	}
	
	public MedooSecurityPolicy getPolicy(){
		return policy;
	}
	
	
	/**
	 * Setter for bean injection
	 * @param policy
	 */
	public void setMedooPolicy(MedooSecurityPolicy policy){
		this.policy = policy;
	}

}
