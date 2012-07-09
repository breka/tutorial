package org.imogene.web.server.dao.hibernate;

import java.io.File;
import java.util.Date;

import org.imogene.web.gwt.common.entity.CommonConstants;
import org.imogene.web.server.binary.Binary;
import org.imogene.web.server.binary.BinaryDao;
import org.imogene.web.server.binary.file.BinaryFile;
import org.imogene.web.server.binary.file.BinaryFileManager;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;





/**
 * Manage persistence for BinaryFile
 */
public class BinaryFileDaoHibernate extends HibernateDaoSupport implements BinaryDao {
	
    /**
     * See BinaryDao interface
     */
    public void saveOrUpdateBinary(Binary bean) {   
    	bean.setModifiedFrom(CommonConstants.IS_WEB);
    	bean.setUploadDate(new Date(System.currentTimeMillis()));
        getHibernateTemplate().saveOrUpdate(bean);
    }

    /**
     * See BinaryDao interface
     */
    public BinaryFile getBinary(String id) {
        BinaryFile result = (BinaryFile) getHibernateTemplate().get(BinaryFile.class, id);
        return result;
    }
    
    /**
     * See BinaryDao interface
     */    
    public void deleteBinary(Binary bean) {    
    	
    	// Delete attached file
    	BinaryFile binary = (BinaryFile)bean;
    	File attachedFile = new File(BinaryFileManager.getInstance().buildFilePath(binary.getId(), binary.getFileName()));
    	attachedFile.delete();
    	
    	File attachedThumbFile = new File(BinaryFileManager.getInstance().buildFilePath("thumb_" + binary.getId(), binary.getFileName()));
    	attachedThumbFile.delete();
    	
    	// Delete binary bean
        getHibernateTemplate().delete(bean);    
    }
    
    
}
