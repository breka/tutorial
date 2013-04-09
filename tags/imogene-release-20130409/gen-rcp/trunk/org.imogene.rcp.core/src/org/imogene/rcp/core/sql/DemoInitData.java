package org.imogene.rcp.core.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DemoInitData {

	public static void initData(Connection conn) {
		DemoInitData.initDB(conn);
	}
	
	private static void initDB(Connection conn){
		try {
			Statement stat = conn.createStatement();
			/* create patient table 
			stat.executeUpdate("drop table if exists patient");*/
			stat
					.executeUpdate("create table patient (id, name, firstname, birthday, age, sex, address, phonenumber, modified, created, modifiedby)");
			/* create table medical actor 
			stat.executeUpdate("drop table if exists medicalactor");*/
			stat
					.executeUpdate("create table medicalactor (id, name, firstname, address, phonenumber, type, modified, created)");
			/* create table medical forms 
			stat.executeUpdate("drop table if exists medicalconsultation");*/
			stat
					.executeUpdate("create table medicalconsultation (id, username, location, team, patient, history, injuryexam, injurydesc, glasgowscore, pupilreaction, heartpulse, saturation, systolic, diastolic, temperature, glycemia, breathrate, photo1, photo2, ecg, treatment, request, modified, created, modifiedby)");
			/* create table medical response 
			stat.executeUpdate("drop table if exists medicalresponse");*/
			stat
					.executeUpdate("create table medicalresponse (id, id_patient, id_medicalactor, id_medicalform, response, modified, created)");
			stat.executeUpdate("drop table if exists synchistory");
			stat
					.executeUpdate("create table synchistory (id, time, status, countsent, countreceived)");
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
