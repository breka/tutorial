package org.imogene.android.util.content;

import java.util.ArrayList;
import java.util.HashMap;

import org.imogene.android.Constants.Viewer;
import org.imogene.android.W;
import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.util.FormatHelper;

import android.content.Context;
import android.content.res.Resources;

public class EntityUtils {
	
	public static ArrayList<HashMap<String, String>> buildInfo(final Context context, final Entity entity) {
		Resources res = context.getResources();
		final ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> id = new HashMap<String, String>();
		id.put(Viewer.INFO_DESC, res.getString(W.string.entity_id));
		id.put(Viewer.INFO_VALUE, entity.getId());
		result.add(id);

		HashMap<String, String> created = new HashMap<String, String>();
		created.put(Viewer.INFO_DESC, res.getString(W.string.created));
		created.put(Viewer.INFO_VALUE, FormatHelper.displayAsDateTime(entity.getCreated()));
		result.add(created);

		HashMap<String, String> createdBy = new HashMap<String, String>();
		createdBy.put(Viewer.INFO_DESC, res.getString(W.string.created_by));
		createdBy.put(Viewer.INFO_VALUE, entity.getCreatedBy());
		result.add(createdBy);

		HashMap<String, String> modified = new HashMap<String, String>();
		modified.put(Viewer.INFO_DESC, res.getString(W.string.modified));
		modified.put(Viewer.INFO_VALUE, FormatHelper.displayAsDateTime(entity.getModified()));
		result.add(modified);

		HashMap<String, String> modifiedBy = new HashMap<String, String>();
		modifiedBy.put(Viewer.INFO_DESC, res.getString(W.string.modified_by));
		modifiedBy.put(Viewer.INFO_VALUE, entity.getModifiedBy());
		result.add(modifiedBy);

		HashMap<String, String> modifiedFrom = new HashMap<String, String>();
		modifiedFrom.put(Viewer.INFO_DESC, res.getString(W.string.modified_from));
		modifiedFrom.put(Viewer.INFO_VALUE, entity.getModifiedFrom());
		result.add(modifiedFrom);

		return result;
	}

}
