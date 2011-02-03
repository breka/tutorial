package org.imogene.testsynchro.onsil;

import org.apache.log4j.Logger;
import org.imogene.common.data.handler.DataHandlerManager;


public class EntityTestFactory extends Thread {
	
	private Logger logger = Logger.getLogger("org.imogene.generated.test.onsil.EntityTestFactory");

	private boolean runTest = true;
	
	private DataHandlerManager manager; 
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		
		while(runTest){
			try{
				DemoInitData.addMedicalCenter(manager);
				sleep(600000);				
			}catch(Exception ex){
				logger.error(ex.getMessage());
			}
		}
	}
	
	public void stopTest(){
		runTest = false;
	}
	
	public void setDataHandlerManager(DataHandlerManager datahandlerManager){
		manager = datahandlerManager;
	}

}
