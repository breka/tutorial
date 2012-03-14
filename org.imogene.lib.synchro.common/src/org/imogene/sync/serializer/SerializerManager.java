package org.imogene.sync.serializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerManager {

	private Map<String, ImogSerializer> serializers = new HashMap<String, ImogSerializer>();

	public void setSerializers(Map<String, ImogSerializer> serializers) {
		this.serializers = serializers;
	}
	
	public ImogSerializer getSerializer(String type){
		return serializers.get(type);
	}
	
}
