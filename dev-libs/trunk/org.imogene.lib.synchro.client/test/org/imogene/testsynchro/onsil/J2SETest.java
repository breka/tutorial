package org.imogene.testsynchro.onsil;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.imogene.common.data.SynchronizableUtil;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.client.BeanKeyGenerator;
import org.imogene.sync.client.DirectSender;
import org.imogene.sync.client.SyncParameters;
import org.imogene.sync.client.Synchronizer;
import org.imogene.sync.client.binary.BinaryFile;
import org.imogene.sync.client.binary.BinaryFileConverter;
import org.imogene.sync.client.binary.BinaryFileHandler;
import org.imogene.sync.client.binary.BinaryFileHibernateDao;
import org.imogene.sync.client.binary.BinaryFileManager;
import org.imogene.sync.client.dao.sqlite.HistoryDaoHibernate;
import org.imogene.sync.client.dao.sqlite.SyncParameterDaoHibernate;
import org.imogene.sync.client.dao.sqlite.SynchronizableEntityDaoHibernate;
import org.imogene.sync.client.http.SyncClientHttp;
import org.imogene.sync.serializer.ImogSerializer;
import org.imogene.sync.serializer.SerializerManager;
import org.imogene.sync.serializer.xml.AssociationConverter;
import org.imogene.sync.serializer.xml.ClassConverter;
import org.imogene.sync.serializer.xml.CollectionConverter;
import org.imogene.sync.serializer.xml.ImogXmlSerializer;
import org.imogene.sync.serializer.xml.PropertyConverter;
import org.imogene.testsynchro.dao.hibernate.DoctorDao;
import org.imogene.testsynchro.dao.hibernate.MedicalCenterDao;
import org.imogene.testsynchro.dao.hibernate.MedicalRequestDao;
import org.imogene.testsynchro.dao.hibernate.PatientDao;
import org.imogene.testsynchro.entity.Doctor;
import org.imogene.testsynchro.entity.MedicalCenter;
import org.imogene.testsynchro.entity.MedicalRequest;
import org.imogene.testsynchro.entity.Patient;
import org.imogene.testsynchro.handler.DoctorHandler;
import org.imogene.testsynchro.handler.MedicalCenterHandler;
import org.imogene.testsynchro.handler.MedicalRequestHandler;
import org.imogene.testsynchro.handler.PatientHandler;
import org.imogene.testsynchro.serializer.xml.DoctorUserConverter;
import org.imogene.uao.synchronizable.SynchronizableEntity;


/**
 * This call enables to test 
 * the J2SE synchronization client.
 * @author MEDES-IMPS
 */
public class J2SETest {
	
	private static Logger logger = Logger.getLogger("J2SETest");
	
	private static int FACTORY_MODE = 1;
	
	private static int MODIFIER_MODE = 2;
	
	private static int BOTH_MODE = 3;
	
	public static String TERMINAL_ID = "scrat";
	public static String LOGIN = "arnaud";
	public static String PASSWORD = "toto";
	public static String URL = "http://192.168.0.70:8080/ImogSynchro/";
	
	public static final String BINARY_PATH = "/home/yann/Desktop/medoo_client/";	
	
	private SerializerManager serializerManager;
	
	private DataHandlerManager dataHandler;
	
	private SyncParameterDaoHibernate parametersDao;
	
	private HistoryDaoHibernate historyDao;
	
	private static int currentMode=0;
	
	
	public J2SETest(){
		
		BinaryFileManager.getInstance().setBinary_file_dir(BINARY_PATH);
		
		setDataHandler();
		
		setSerializer();
		
		/* initialize the demonstration data */		
		//DemoInitData.addMedicalCenter(dataHandler);				
		
		setParameters();
		initShortNameMapper();	
		/* Initialize the synchronization process */
		SyncParameters param = parametersDao.load();
		DirectSender ds = new DirectSender();
		ds.setDataManager(dataHandler);
		ds.setParametersDao(parametersDao);
		ds.setSyncClient(new SyncClientHttp(param.getUrl()));
		//ds.start();
		
		/* start the synchronizer thread */
		Synchronizer s= new Synchronizer();	
		s.setDataManager(dataHandler);	
		s.setSerializerManager(serializerManager);
		s.setHistoryDao(historyDao);
		s.setSyncClient(new SyncClientHttp(param.getUrl()));
		s.setSyncParametersDao(parametersDao);
		s.start();
		
		if(currentMode==FACTORY_MODE || currentMode == BOTH_MODE)
			runFactory();
		if(currentMode==MODIFIER_MODE || currentMode == BOTH_MODE)
			runModifier();
	}
	
	private void initShortNameMapper(){
		Map<String, String> entityClassReferences = new HashMap<String, String>();
		entityClassReferences.put("DOC", Doctor.class.getName());
		SynchronizableUtil.getInstance().setEntityClassReferences(entityClassReferences);
	}
	
	private SynchronizableEntity addEntity(Class<?> classToAdd){
		SynchronizableEntity entity = new SynchronizableEntity();
		entity.setId(BeanKeyGenerator.generateKeyId());
		entity.setName(classToAdd.getName());
		entity.setModified(new Date(System.currentTimeMillis()));
		entity.setModifiedBy(LOGIN);
		SynchronizableEntityDaoHibernate dao = new SynchronizableEntityDaoHibernate();
		dao.store(entity);
		return entity;
	}
	
	private void setParameters(){
		parametersDao = new SyncParameterDaoHibernate();		
		historyDao = new HistoryDaoHibernate(); 		
		SyncParameters paramDefault = new SyncParameters();
		paramDefault.setLogin(LOGIN);
		paramDefault.setPassword(PASSWORD);
		paramDefault.setModifiedFrom(TERMINAL_ID);
		
		Set<SynchronizableEntity> directSend = new HashSet<SynchronizableEntity>();
		directSend.add(addEntity(MedicalRequest.class));
		
		Set<SynchronizableEntity> toSync = new HashSet<SynchronizableEntity>();
		toSync.add(addEntity(Doctor.class));
		toSync.add(addEntity(MedicalCenter.class));
		toSync.add(addEntity(Patient.class));
		toSync.add(addEntity(BinaryFile.class));
		
		paramDefault.setDirectSend(directSend);
		paramDefault.setSynchronizable(toSync);
		paramDefault.setType("xml");
		paramDefault.setUrl(URL);
		parametersDao.store(paramDefault);
	}
	
	/**
	 * Set and configure the data handler
	 */
	private void setDataHandler(){
		dataHandler = new DataHandlerManager();
		Map<String, EntityHandler> handlers = new HashMap<String, EntityHandler>();
		
		/*patient*/
		PatientHandler pHandler = new PatientHandler();		
		pHandler.setDao(new PatientDao());		
		handlers.put("org.imogene.testsynchro.entity.Patient", pHandler);
		
		/* doctor */
		DoctorHandler dHandler = new DoctorHandler();				
		dHandler.setDao(new DoctorDao());		
		handlers.put("org.imogene.testsynchro.entity.Doctor", dHandler);		
		/* doctorUser */	
		handlers.put("org.imogene.testsynchro.entity.DoctorUser", dHandler);
		
		/* medical request */
		MedicalRequestHandler mdHandler = new MedicalRequestHandler();		
		mdHandler.setDao(new MedicalRequestDao());
		handlers.put("org.imogene.testsynchro.entity.MedicalRequest", mdHandler);
		
		/* medical center */		
		MedicalCenterHandler mcHandler = new MedicalCenterHandler();		
		mcHandler.setDao(new MedicalCenterDao());
		handlers.put(MedicalCenter.class.getName(), mcHandler);
		
		/* binary handler */
		BinaryFileHandler binaryHandler = new BinaryFileHandler();
		Map<String, String> entityClassReferences = new HashMap<String, String>();		
		binaryHandler.setEntityClassReferences(entityClassReferences);
		binaryHandler.setDao(new BinaryFileHibernateDao());
		binaryHandler.setDataHandlerManager(dataHandler);
		handlers.put(BinaryFile.class.getName(), binaryHandler);
		
		dataHandler.setHandlers(handlers);			
	}
	
	
	/**
	 * Initialize the XML serializer
	 */
	private void setSerializer(){
		serializerManager = new SerializerManager();
		ImogXmlSerializer xmlSerializer = new ImogXmlSerializer();
		xmlSerializer.setDataHandlerManager(dataHandler);
										
		AssociationConverter associationConverter = new AssociationConverter();
		associationConverter.setDataHandlerManager(dataHandler);
		CollectionConverter collectionConverter = new CollectionConverter();
		collectionConverter.setDataHandlerManager(dataHandler);
		
		Set<PropertyConverter> converters = new HashSet<PropertyConverter>();
		Set<ClassConverter> specificConverters = new HashSet<ClassConverter>();
		
		BinaryFileConverter binaryConverter = new BinaryFileConverter();
		ClassConverter binaryClassConverter = new ClassConverter();
		binaryClassConverter.setAlias("org.imogene.data.Binary");
		binaryClassConverter.setClassType("org.imogene.sync.client.binary.BinaryFile");
		binaryClassConverter.setConverter(binaryConverter);
		
		DoctorUserConverter doctorUserConverter = new DoctorUserConverter();
		ClassConverter doctorUserClassConverter = new ClassConverter();
		doctorUserClassConverter.setAlias("org.imogene.testsynchro.entity.DoctorUser");
		doctorUserClassConverter.setClassType("org.imogene.testsynchro.entity.DoctorUser");
		doctorUserClassConverter.setConverter(doctorUserConverter);
		
		specificConverters.add(binaryClassConverter);
		specificConverters.add(doctorUserClassConverter);
				
		PropertyConverter medicalRequestPatientConverter = new PropertyConverter();
		medicalRequestPatientConverter.setClassName(MedicalRequest.class.getName());
		medicalRequestPatientConverter.setConverter(associationConverter);
		medicalRequestPatientConverter.setPropertyName("medicalRequestPatient");
		converters.add(medicalRequestPatientConverter);
		
		PropertyConverter medicalRequestDoctorConverter = new PropertyConverter();
		medicalRequestDoctorConverter.setClassName(MedicalRequest.class.getName());
		medicalRequestDoctorConverter.setConverter(associationConverter);
		medicalRequestDoctorConverter.setPropertyName("medicalRequestDoctor");
		converters.add(medicalRequestDoctorConverter);
		
		PropertyConverter medicalRequestMedicalCenterConverter = new PropertyConverter();
		medicalRequestMedicalCenterConverter.setClassName(MedicalRequest.class.getName());
		medicalRequestMedicalCenterConverter.setConverter(associationConverter);
		medicalRequestMedicalCenterConverter.setPropertyName("medicalRequestMedicalCenter");
		converters.add(medicalRequestMedicalCenterConverter);
		
		PropertyConverter patientMedicalCenterConverter = new PropertyConverter();
		patientMedicalCenterConverter.setClassName(Patient.class.getName());
		patientMedicalCenterConverter.setConverter(associationConverter);
		patientMedicalCenterConverter.setPropertyName("patientMedicalCenter");
		converters.add(patientMedicalCenterConverter);
		
		PropertyConverter doctorMedicalCenterConverter = new PropertyConverter();
		doctorMedicalCenterConverter.setClassName(Doctor.class.getName());
		doctorMedicalCenterConverter.setConverter(associationConverter);
		doctorMedicalCenterConverter.setPropertyName("doctorMedicalCenter");
		converters.add(doctorMedicalCenterConverter);
		
		PropertyConverter medicalCenterDoctorConverter = new PropertyConverter();
		medicalCenterDoctorConverter.setClassName(MedicalCenter.class.getName());
		medicalCenterDoctorConverter.setConverter(collectionConverter);
		medicalCenterDoctorConverter.setPropertyName("medicalCenterDoctors");
		converters.add(medicalCenterDoctorConverter);
				
		xmlSerializer.setClassConverters(specificConverters);
		xmlSerializer.setPropertyConverters(converters);
		Map<String, ImogSerializer> serializers = new HashMap<String, ImogSerializer>();
		serializers.put("xml", xmlSerializer);
		serializerManager.setSerializers(serializers);
	}
		
	private void runFactory(){
		EntityTestFactory factory = new EntityTestFactory();
		factory.setDataHandlerManager(dataHandler);
		factory.start();
	}
	
	private void runModifier(){
		EntityTestModifier modifier = new EntityTestModifier();
		modifier.setDataHandlerManager(dataHandler);
		modifier.start();
	}
	
	public static void main(String[] args){
		currentMode = FACTORY_MODE;
		if(args.length>1 && args[0].equals("modifier")){
				currentMode = MODIFIER_MODE;
				logger.debug("Launching the modifier");
		}
		if(args.length>1 && args[0].equals("both")){			
				currentMode = BOTH_MODE;
				logger.debug("Launching both");
		}
		new J2SETest();
	}
}
