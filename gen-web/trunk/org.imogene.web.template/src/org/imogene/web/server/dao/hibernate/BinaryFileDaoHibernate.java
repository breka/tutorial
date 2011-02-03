package org.imogene.web.server.dao.hibernate;

import java.io.File;
import java.util.Date;

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
    	bean.setModifiedFrom("web");
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
    	
    	// Delete binary bean
        getHibernateTemplate().delete(bean);    
    }
    
    
}
