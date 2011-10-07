/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.imogene.android.app;

import org.imogene.android.util.app.ActivityHelper;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * A base activity that defers common functionality across app activities to an
 * {@link ActivityHelper}. This class shouldn't be used directly; instead,
 * activities should inherit from {@link BaseSinglePaneActivity} or
 * {@link BaseMultiPaneActivity}.
 */
public abstract class BaseExpandableListActivity extends ExpandableListActivity {

	final ActivityHelper mActivityHelper = ActivityHelper.createInstance(this);

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mActivityHelper.onPostCreate(savedInstanceState);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		return mActivityHelper.onKeyLongPress(keyCode, event)
				|| super.onKeyLongPress(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return mActivityHelper.onKeyDown(keyCode, event)
				|| super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		switch (featureId) {
		case ActivityHelper.FEATURE_ACTION_BAR_PANEL:
			return onCreateActionBarMenu(menu);
		default:
			return super.onCreatePanelMenu(featureId, menu);
		}
	}
	
	public boolean onCreateActionBarMenu(Menu menu) {
		return mActivityHelper.onCreateActionBarMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (featureId) {
		case ActivityHelper.FEATURE_ACTION_BAR_PANEL:
			return onActionBarItemSelected(item);
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	public boolean onActionBarItemSelected(MenuItem item) {
		return mActivityHelper.onActionBarItemSelected(item);
	}

	/**
	 * Returns the {@link ActivityHelper} object associated with this activity.
	 */
	protected ActivityHelper getActivityHelper() {
		return mActivityHelper;
	}

}
