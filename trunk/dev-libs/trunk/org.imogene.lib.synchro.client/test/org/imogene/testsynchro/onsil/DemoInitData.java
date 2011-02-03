package org.imogene.testsynchro.onsil;

import java.io.IOException;
import java.util.Date;

import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.sync.client.BeanKeyGenerator;
import org.imogene.sync.client.binary.BinaryFile;
import org.imogene.testsynchro.entity.Doctor;
import org.imogene.testsynchro.entity.MedicalCenter;


public class DemoInitData {	
	
	public static int COUNT = 0;	
	
	
	public static void addMedicalCenter(DataHandlerManager handlerManager){	
		COUNT = COUNT + 1;
		MedicalCenter center = new MedicalCenter();
		center.setId(BeanKeyGenerator.getNewId("CEN"));
		center.setCode("CEN_" + COUNT);
		center.setName("Imogene clinique");
		center.setCreated(new Date(System.currentTimeMillis()));
		center.setCreatedBy(J2SETest.LOGIN);
		center.setModifiedBy(J2SETest.LOGIN);
		center.setModifiedFrom(J2SETest.TERMINAL_ID);
		center.setModified(new Date(System.currentTimeMillis()));
		handlerManager.getHandler(MedicalCenter.class.getName()).saveOrUpdate(center, null);
		
		Doctor doctor = new Doctor();
		String doctorId = BeanKeyGenerator.getNewId("DOC");
		doctor.setId(doctorId);
		doctor.setFirstName("Toto");
		doctor.setName("DOC_"+COUNT);
		doctor.setMobileNumber("0123456789");
		doctor.setDoctorMedicalCenter(center);
		doctor.setCreated(new Date(System.currentTimeMillis()));
		doctor.setCreatedBy(J2SETest.LOGIN);
		doctor.setModifiedBy(J2SETest.LOGIN);
		doctor.setModifiedFrom(J2SETest.TERMINAL_ID);
		doctor.setModified(new Date(System.currentTimeMillis()));
		doctor.setPhoto("BIN_"+ doctorId);
		handlerManager.getHandler(Doctor.class.getName()).saveOrUpdate(doctor, null);
		TestTools.copyFile("photo.png", "BIN_"+doctorId, "photo.png");	
		
		BinaryFile file = new BinaryFile();
		file.setCreated(new Date(System.currentTimeMillis()));
		file.setModified(new Date(System.currentTimeMillis()));
		file.setCreatedBy(J2SETest.LOGIN);
		file.setModifiedBy(J2SETest.LOGIN);
		file.setModifiedFrom(J2SETest.TERMINAL_ID);
		file.setId("BIN_"+ doctorId);	
		file.setFileName("photo.png");
		try {
			file.setLength(file.createInputStream().available());
		} catch (IOException e) {
			e.printStackTrace();
		}
		file.setParentFieldGetter("getPhoto");
		file.setParentEntity("DOC");
		file.setParentKey(doctor.getId());	
		handlerManager.getHandler(BinaryFile.class.getName()).saveOrUpdate(file, null);
	}
}
