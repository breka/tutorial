

package org.imogene.web.gwt.client.i18n;

import com.google.gwt.i18n.client.Constants;

public interface ImogConstants extends Constants {

	/* ------------------------------------------------------------------- */
	/*                         COMMON TEXTS                                */
	/* ------------------------------------------------------------------- */

	/* Bottom links */
	String bottom_link_legal();
	String bottom_link_copyrights();
	String bottom_link_contact();
	
	/* Boolean texts */
	String boolean_true();
	String boolean_false();
	String boolean_unknown();
	
	/* Enumeration texts */
	String enumeration_unknown();
	
	/* QRCode text */
	String qrcode();	
	
	/* Date text */
	String format_date();
	String format_time();
	String format_validation_date();
	String format_validation_time();
	
	/* Error messages */
	String field_mandatory();
	String field_correct_email();
	String field_correct_integer();
	String field_correct_float();
	String thema_default();
	
	/* form messages */
	String form_saving();
	String form_uploading();
	String form_saved();
	String form_not_validated();
	String form_loading();
	String form_metadata_title();
	
	/* form buttons */
	String button_save();
	String button_edit();
	String button_cancel();
	String button_close();
	String button_print();
	String button_delete();
	String button_create();
	String button_ok();
	String button_assign();
	String button_add();
	String button_remove();
	String button_view ();
	String button_search ();
	
	/* login panel */
	String login_login();
	String login_password();
	String login_button();
	String login_description();
	String login_identify();
	String login_wrong_id();
	String login_expired();
	
	/* top panel */
	String disconnect();
	
	/* list */
	String entity_card_number_txt();
	
	/* binary field */
	String binary_nofile();
	String binary_select();
	
	/* confirmation texts */
	String confirmation_delete();
	String confirmation_delete_several1();
	String confirmation_delete_several2();
	String confirmation_save();
}
