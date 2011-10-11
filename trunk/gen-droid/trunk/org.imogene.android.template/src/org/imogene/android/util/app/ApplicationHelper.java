package org.imogene.android.util.app;

public class ApplicationHelper {

	
	public static final void setHomeClass(Class<?> homeClass) {
		ApplicationController.get().setHomeClass(homeClass);
	}
	
	public static final Class<?> getHomeClass() {
		return ApplicationController.get().getHomeClass();
	}
	
	private static class ApplicationController {
		
		private static ApplicationController sInstance;
		
		public static final ApplicationController get() {
			if (sInstance == null) {
				sInstance = new ApplicationController();
			}
			return sInstance;
		}
		
		private Class<?> mHomeClass;

		public void setHomeClass(Class<?> homeClass) {
			mHomeClass = homeClass;
		}
		
		public Class<?> getHomeClass() {
			return mHomeClass;
		}
	}
	
	
	
	
}
