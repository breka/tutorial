package org.imogene.gwt.widgets.client.dynaTable;

import com.google.gwt.user.client.Timer;

public class AutoRefreshDynaTableWidget extends DynaTableWidget{
	
	public static int DEFAULT_REFRESH_PERIOD = 5000;
	
	private int refreshPeriod;
	
	private Timer refreshTimer;
	
	/**
	 * Create autorefresh table, with 5s as periodRefresh.
	 * @param provider
	 * @param columns
	 * @param columnStyles
	 * @param rowCount
	 */
	public AutoRefreshDynaTableWidget(DynaTableDataProvider provider, ColumnHeader[] columns,
			String[] columnStyles, int rowCount){
		this(provider,columns,columnStyles,rowCount, DEFAULT_REFRESH_PERIOD);		
	}
	
	/**
	 * Create autorefresh table.
	 * @param provider
	 * @param columns
	 * @param columnStyles
	 * @param rowCount
	 * @param refreshPeriod
	 */
	public AutoRefreshDynaTableWidget(DynaTableDataProvider provider, ColumnHeader[] columns,
			String[] columnStyles, int rowCount, int refreshPeriod){
		super(provider,columns,columnStyles,rowCount);
		createTimer(refreshPeriod);		
		this.refreshPeriod = refreshPeriod; 
	}
	
	/**
	 * 
	 * @param refreshPeriod
	 */
	private void createTimer(int refreshPeriod){
		refreshTimer = new Timer(){
			public void run(){
				refresh();
			}
		};
		refreshTimer.scheduleRepeating(refreshPeriod);		
	}
	
	/**
	 * Get the timer resfresh period
	 */
	public int getRefreshPeriod(){
		return refreshPeriod;
	}
	
	/**
	 * set the timer refresh period.
	 * @param refreshPeriod
	 */
	public void setResfreshPeriod(int refreshPeriod){		
		if(this.refreshTimer != null){
			refreshTimer.scheduleRepeating(refreshPeriod);			
		}
		this.refreshPeriod = refreshPeriod;
	}

}
