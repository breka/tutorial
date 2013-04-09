package org.imogene.web.gwt.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface ImogMessages extends Messages{
	
	String error_format_date(String fieldName, String format);
	String error_format_time(String fieldName, String format);
	String error_format_datetime(String fieldName, String format);
	String error_format_float(String fieldName, String format);
	String error_format_int(String fieldName, String format);
	String error_format_email(String fieldName);
	String error_format(String fieldName, String formatText);
	String error_required(String fieldName);
	String error_min_date(String fieldName, String format);
	String error_max_date(String fieldName, String format);
	String error_min_num(String fieldName, String format);
	String error_max_num(String fieldName, String format);
	String error_float_dec(String fieldName, String decNb);
	
	String banner_user_label(String currentUser);
	String banner_lastlogindate_label(String lastLoginDate);
	
	String warning_card_filtered(String entityName);
	
	/* form composite */
	String form_modification_title(String entityName);
	String form_metadata_id(String id);
	String form_metadata_creation(String creationDate, String creator);
	String form_metadata_modification(String modificationDate, String modifier);
	String form_metadata_current_user(String currentUser);
	
	String error_num_range(String minValue, String maxValue);
	String error_num_min(String minValue);
	String error_num_max(String maxValue);
	
	/* Binary fields messages */
	String field_binary_file(String fileName);
	String field_binary_size(String fileSize);
	
	
}
