package org.imogene.web.server.handler;

import java.io.File;
import java.util.Date;

import org.imogene.lib.common.binary.Binary;
import org.imogene.lib.common.binary.BinaryDao;
import org.imogene.lib.common.binary.file.BinaryFile;
import org.imogene.lib.common.binary.file.BinaryFileManager;
import org.imogene.lib.common.constants.CommonConstants;
import org.imogene.lib.common.entity.ImogActor;
import org.imogene.web.server.util.BinaryDesc;
import org.imogene.web.server.util.HttpSessionUtil;
import org.imogene.web.server.util.ServerConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * Binary Handler
 * @author Medes-IMPS
 */
public class BinaryHandler  {

	private BinaryDao<Binary> dao;

	/**
	 * 
	 * @param bean
	 */
	@Transactional
    public void saveOrUpdateBinary(Binary bean) { 
		
		ImogActor actor = (ImogActor) HttpSessionUtil.getHttpSession().getAttribute(ServerConstants.SESSION_USER);
    	
		bean.setCreated(new Date(System.currentTimeMillis()));
		bean.setCreatedBy(actor.getLogin());
		bean.setModified(new Date(System.currentTimeMillis()));
		bean.setModifiedBy(actor.getLogin());
		bean.setModifiedFrom(CommonConstants.IS_WEB);
		
    	dao.saveOrUpdate(bean, true);
    }
	
    /**
     * 
     * @param binaryId
     * @return
     */
	@Transactional(readOnly = true)
	public BinaryDesc getBinaryDesc(String binaryId) {

		Binary binary = dao.load(binaryId);
		if (binary != null) {
			BinaryDesc desc = new BinaryDesc();
			desc.setId(binary.getId());
			desc.setName(binary.getFileName());
			desc.setSize(binary.getLength());
			return desc;
		}
		return null;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public BinaryFile getBinary(String id) {
		return (BinaryFile)dao.load(id);
	}
	
	
	/**
	 * 
	 * @param bean
	 */
	@Transactional
	public void deleteBinary(Binary bean) {
    	// Delete attached file
    	BinaryFile binary = (BinaryFile)bean;
    	File attachedFile = new File(BinaryFileManager.getInstance().buildFilePath(binary.getId(), binary.getFileName()));
    	attachedFile.delete();
    	
    	File attachedThumbFile = new File(BinaryFileManager.getInstance().buildFilePath("thumb_" + binary.getId(), binary.getFileName()));
    	attachedThumbFile.delete();
    	
    	// Delete binary bean
    	dao.delete(bean);
	}

	/**
	 * Setter for bean injection
	 * @param binaryDao
	 */
	public void setDao(BinaryDao<Binary> binaryDao) {
		this.dao = binaryDao;
	}
	
	

}
