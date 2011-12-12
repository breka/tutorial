package org.imogene.android.util.app;

import org.imogene.android.template.R;
import org.imogene.android.view.SimpleMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityHelper {
	
	public static final int FEATURE_ACTION_BAR_PANEL = 123;
	
	private Activity mActivity;
	
    /**
     * Factory method for creating {@link ActivityHelper} objects for a given activity. Depending
     * on which device the app is running, either a basic helper or Honeycomb-specific helper will
     * be returned.
     */
    public static ActivityHelper createInstance(Activity activity) {
        return new ActivityHelper(activity);
    }

    protected ActivityHelper(Activity activity) {
        mActivity = activity;
    }
	
    public void onPostCreate(Bundle savedInstanceState) {
    	setupActionBar(mActivity.getTitle(), 0);
        // Create the action bar
        SimpleMenu menu = new SimpleMenu(mActivity);
        mActivity.onCreatePanelMenu(FEATURE_ACTION_BAR_PANEL, menu);
        // TODO: call onPreparePanelMenu here as well
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            addActionButtonCompatFromMenuItem(item);
        }
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return false;
        }
        return false;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goHome();
            return true;
        }
        return false;
    }
    
    public boolean onCreateActionBarMenu(Menu menu) {
    	return false;
    }
    
	public boolean onActionBarItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ig_menu_search:
			goSearch();
			return true;
		}
		return false;
	}
    
    /**
     * Invoke "home" action, returning to {@link com.google.android.apps.iosched.ui.HomeActivity}.
     */
    public void goHome() {
//        if (mActivity instanceof HomeActivity) {
//            return;
//        }
//
//        final Intent intent = new Intent(this, HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    	
    	if (ApplicationHelper.getHomeClass().equals(mActivity.getClass())) {
    		return;
    	}
    	
    	final Intent intent = new Intent(mActivity, ApplicationHelper.getHomeClass());
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	mActivity.startActivity(intent);
    }

    /**
     * Invoke "search" action, triggering a default search.
     */
    public void goSearch() {
        mActivity.startSearch(null, false, Bundle.EMPTY, false);
    }

    /**
     * Sets up the action bar with the given title and accent color. If title is null, then
     * the app logo will be shown instead of a title. Otherwise, a home button and title are
     * visible. If color is null, then the default colorstrip is visible.
     */
    public void setupActionBar(CharSequence title, int color) {
        final ViewGroup actionBarCompat = getActionBarCompat();
        if (actionBarCompat == null) {
            return;
        }

        LinearLayout.LayoutParams springLayoutParams = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.FILL_PARENT);
        springLayoutParams.weight = 1;

        View.OnClickListener homeClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                goHome();
            }
        };

        if (title != null) {
            // Add Home button
        	// addActionButtonCompat(R.drawable.ic_title_home, R.string.ig_menu_home,
        	//        homeClickListener, true);

            // Add title text
            TextView titleText = new TextView(mActivity, null, R.attr.actionbarCompatTextStyle);
            titleText.setLayoutParams(springLayoutParams);
            titleText.setText(title);
            titleText.setOnClickListener(homeClickListener);
            actionBarCompat.addView(titleText);

        } else {
            // Add logo
            ImageButton logo = new ImageButton(mActivity, null, R.attr.actionbarCompatLogoStyle);
            logo.setOnClickListener(homeClickListener);
            actionBarCompat.addView(logo);

            // Add spring (dummy view to align future children to the right)
            View spring = new View(mActivity);
            spring.setLayoutParams(springLayoutParams);
            actionBarCompat.addView(spring);
        }

        setActionBarColor(color);
    }
    
    /**
     * Set action bar clickable or not
     * @param clickable
     */
    public void setActionBarClickable(boolean clickable) {
    	final View title = mActivity.findViewById(R.id.ig_actionbar_compat_text);
    	if (title == null) {
    		return;
    	}
    	title.setClickable(clickable);
    }

    /**
     * Sets the action bar color to the given color.
     */
    public void setActionBarColor(int color) {
        if (color == 0) {
            return;
        }

        final View colorstrip = mActivity.findViewById(R.id.ig_colorstrip);
        if (colorstrip == null) {
            return;
        }

        colorstrip.setBackgroundColor(color);
    }

    /**
     * Sets the action bar title to the given string.
     */
    public void setActionBarTitle(CharSequence title) {
        ViewGroup actionBar = getActionBarCompat();
        if (actionBar == null) {
            return;
        }

        TextView titleText = (TextView) actionBar.findViewById(R.id.ig_actionbar_compat_text);
        if (titleText != null) {
            titleText.setText(title);
        }
    }

    /**
     * Returns the {@link ViewGroup} for the action bar on phones (compatibility action bar).
     * Can return null, and will return null on Honeycomb.
     */
    public ViewGroup getActionBarCompat() {
        return (ViewGroup) mActivity.findViewById(R.id.ig_actionbar_compat);
    }

    /**
     * Adds an action bar button to the compatibility action bar (on phones).
     */
    private View addActionButtonCompat(int iconResId, int textResId,
            View.OnClickListener clickListener, boolean separatorAfter) {
        final ViewGroup actionBar = getActionBarCompat();
        if (actionBar == null) {
            return null;
        }

        // Create the separator
        ImageView separator = new ImageView(mActivity, null, R.attr.actionbarCompatSeparatorStyle);
        separator.setLayoutParams(
                new ViewGroup.LayoutParams(2, ViewGroup.LayoutParams.FILL_PARENT));

        // Create the button
        ImageButton actionButton = new ImageButton(mActivity, null, R.attr.actionbarCompatButtonStyle);
        actionButton.setLayoutParams(new ViewGroup.LayoutParams(
                (int) mActivity.getResources().getDimension(R.dimen.ig_actionbar_compat_height),
                ViewGroup.LayoutParams.FILL_PARENT));
        actionButton.setImageResource(iconResId);
        actionButton.setScaleType(ImageView.ScaleType.CENTER);
        actionButton.setContentDescription(mActivity.getResources().getString(textResId));
        actionButton.setOnClickListener(clickListener);

        // Add separator and button to the action bar in the desired order

        if (!separatorAfter) {
            actionBar.addView(separator);
        }

        actionBar.addView(actionButton);

        if (separatorAfter) {
            actionBar.addView(separator);
        }

        return actionButton;
    }

    /**
     * Adds an action button to the compatibility action bar, using menu information from a
     * {@link MenuItem}. If the menu item ID is <code>menu_refresh</code>, the menu item's state
     * can be changed to show a loading spinner using
     * {@link ActivityHelper#setRefreshActionButtonCompatState(boolean)}.
     */
    private View addActionButtonCompatFromMenuItem(final MenuItem item) {
        final ViewGroup actionBar = getActionBarCompat();
        if (actionBar == null) {
            return null;
        }

        // Create the separator
        ImageView separator = new ImageView(mActivity, null, R.attr.actionbarCompatSeparatorStyle);
        separator.setLayoutParams(
                new ViewGroup.LayoutParams(2, ViewGroup.LayoutParams.FILL_PARENT));

        // Create the button
        ImageButton actionButton = new ImageButton(mActivity, null, R.attr.actionbarCompatButtonStyle);
        actionButton.setId(item.getItemId());
        actionButton.setLayoutParams(new ViewGroup.LayoutParams(
                (int) mActivity.getResources().getDimension(R.dimen.ig_actionbar_compat_height),
                ViewGroup.LayoutParams.FILL_PARENT));
        actionButton.setImageDrawable(item.getIcon());
        actionButton.setScaleType(ImageView.ScaleType.CENTER);
        actionButton.setContentDescription(item.getTitle());
        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mActivity.onMenuItemSelected(FEATURE_ACTION_BAR_PANEL, item);
            }
        });

        actionBar.addView(separator);
        actionBar.addView(actionButton);

        return actionButton;
    }
}
