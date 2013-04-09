package org.imogene.rcp.core.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.eclipse.core.runtime.Platform;

public class ConnectionManager {
private static ConnectionManager instance;
	
	private Connection conn;
	
	/**
	 * 
	 */
	private ConnectionManager(){
		init();		
	}
	
	/**
	 * 
	 * @return
	 */
	public static ConnectionManager getInstance(){
		if(instance == null){
			instance = new ConnectionManager();
			initDB();
		}
		
		return instance;
	}
	
	/**
	 * 
	 */
	private void init(){
		try{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+Platform.getLocation().toOSString()+"/imog.db");
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Connection getConnection(){
		return conn;
	}
	
	/**
	 * 
	 */
	private static void initDB() {
		try {

			Connection conn = ConnectionManager.getInstance().getConnection();
			Statement stat = conn.createStatement();
			/* create synchistory table */
			stat
					.executeUpdate("create table synchistory (id, time, status, countsent, countreceived)");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
