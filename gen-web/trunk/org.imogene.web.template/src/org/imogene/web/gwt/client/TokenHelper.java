package org.imogene.web.gwt.client;

public class TokenHelper {

	public static EntityToken getToken(String token){
		String[] params = token.split("/");
		if(params.length==0)
			return null;
			//throw new RuntimeException("Invalid token: "+token);
		else{
			if((params[0].equals("view") || params[0].equals("edit")) && params.length==3){
				return new EntityToken(params[0], params[1], params[2]);
				
			}else if((params[0].equals("new") || params[0].equals("list")) && params.length==2){				
				return new EntityToken(params[0], params[1],"");
				
			}else if(params[0].equals("specific") && params.length==4){								
				return new EntityToken(params[1], params[2], params[3]);
				
			}else if(params[0].equals("specific") && params.length==3){
				return new EntityToken(params[2], params[2],"");
				
			}else{
				return null;
				//throw new RuntimeException("Invalid token: "+token);
			}
		}
	}
	
	/**
	 * @author Medes-IMPS
	 */
	public static interface ImogToken{
		public String getAction();
	}
	
	/**
	 * @author Medes-IMPS
	 */
	public static class EntityToken implements ImogToken {
		
		private String action;
		
		private String type;
		
		private String id;
		
		public EntityToken(String pAction,  String pType, String pId){
			action = pAction;
			id = pId;
			type = pType;
		}

		public String getAction() {
			return action;
		}

		public String getId() {
			return id;
		}
		
		public String getType(){
			return type;
		}
	}
}
