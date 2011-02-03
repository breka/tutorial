package org.imogene.testsynchro.onsil;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.testsynchro.entity.Doctor;


public class EntityTestModifier extends Thread {

	private boolean runTest = true;
	
	private Logger logger = Logger.getLogger("org.imogene.generated.test.Modifier");
	
	private DataHandlerManager manager; 
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		
		while(runTest){
			try{
				modify();
				sleep(60000);				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	private void modify(){
		int i=0;
		EntityHandler handler = manager.getHandler("org.imogene.testsynchro.entity.Doctor");
		List<Synchronizable>doctors = handler.loadEntities(null);
		for(Synchronizable doctor: doctors){
			Doctor doc = (Doctor)doctor;
			if(!doc.getModifiedFrom().equals(J2SETest.TERMINAL_ID)){
				String name = doc.getName();
				String newName = "modified-"+name;
				doc.setName(newName);
				doc.setModified(new Date(System.currentTimeMillis()));
				doc.setModifiedFrom(J2SETest.TERMINAL_ID);
				doc.setModifiedBy(J2SETest.LOGIN);
				handler.saveOrUpdate(doc, null);
				i++;
			}
		}
		logger.debug("Modified "+i+" doctors.");
	}
	
	public void stopTest(){
		runTest = false;
	}
	
	public void setDataHandlerManager(DataHandlerManager datahandlerManager){
		manager = datahandlerManager;
	}

}
