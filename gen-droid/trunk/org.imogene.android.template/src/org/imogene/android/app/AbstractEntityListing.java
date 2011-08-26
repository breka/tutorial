package org.imogene.android.app;

import java.util.ArrayList;

import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.SortOrder;
import org.imogene.android.Constants.Sync;
import org.imogene.android.W;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.util.IamLost;
import org.imogene.android.util.content.IntentUtils;
import org.imogene.android.util.database.DatabaseUtils;
import org.imogene.android.util.dialog.DialogFactory;
import org.imogene.android.widget.EntityCursorAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

public abstract class AbstractEntityListing extends ListActivity implements
		View.OnClickListener, DialogInterface.OnClickListener {
	
	public interface ListAdapterFactory {
		public EntityCursorAdapter newListAdapter(Context context, EntityCursor cursor, Drawable color, int choiceMode);
	}
	
	public static final SQLiteBuilder computeWhere(SQLiteBuilder builder) {
		SQLiteBuilder result = new SQLiteBuilder();
		if (builder != null)
			result.appendWhere(builder.create());
		return result.appendNotEq(Keys.KEY_MODIFIEDFROM, Sync.SYNC_SYSTEM);
	}

	// Extras keys
	private static final String EXTRA_CURRENT_URI = "AbstractEntityListing_currentUri";

	// Options menu ids
	private static final int MENU_NEW_ID = Menu.FIRST;
	private static final int MENU_MARK_ALL_AS_READ_ID = Menu.FIRST + 1;
	private static final int MENU_IAMLOST_ID = Menu.FIRST + 2;
	private static final int MENU_SORT_BY_MODIFIED_ID = Menu.FIRST + 3;
	private static final int MENU_SEARCH_ID = Menu.FIRST + 4;

	private static final int MENU_SORT_BY_GROUP_ID = 1;

	// Context menu ids
	private static final int CMENU_DELETE_ID = Menu.FIRST;
	private static final int CMENU_EDIT_ID = Menu.FIRST + 1;
	private static final int CMENU_VIEW_ID = Menu.FIRST + 2;
	private static final int CMENU_MARK_AS_READ_ID = Menu.FIRST + 3;
	private static final int CMENU_MARK_AS_UNREAD_ID = Menu.FIRST + 4;

	// Dialog ids
	private static final int DIALOG_DELETE_ID = 1;
	private static final int DIALOG_IAMLOST_ID = 2;

	// Activity request code
	private static final int ACTIVITY_INSERT = 1;

	private EntityCursor mCursor;
	private EntityCursorAdapter mAdapter;

	private final Uri mUri;
	private final Drawable mColor;

	protected boolean mCanCreate;
	protected boolean mCanDelete;
	protected boolean mCanModify;

	protected SQLiteBuilder mSQLBuilder = null;
	protected String mSortKey = Keys.KEY_MODIFIED;
	protected int mSortOrder = SortOrder.DESCENDANT_ORDER;

	private Uri mCurrentUri;
	
	private ListAdapterFactory mListAdapterFactory;

	public AbstractEntityListing(final Uri uri, final Drawable color) {
		mUri = uri;
		mColor = color;
	}
	
	public void setListAdapterFactory(ListAdapterFactory factory) {
		mListAdapterFactory = factory;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			initWhereForSearch();
		}

		init();

		IamLost.getInstance().add(getTitle().toString());

		if (getIntent().hasExtra(Extras.EXTRA_WHERE))
			mSQLBuilder = getIntent().getParcelableExtra(Extras.EXTRA_WHERE);

		if (getIntent().hasExtra(Extras.EXTRA_SORT_KEY))
			mSortKey = getIntent().getStringExtra(Extras.EXTRA_SORT_KEY);

		if (getIntent().hasExtra(Extras.EXTRA_SORT_ORDER))
			mSortOrder = getIntent().getIntExtra(Extras.EXTRA_SORT_ORDER,
					SortOrder.DESCENDANT_ORDER);

		if (savedInstanceState != null) {
			mSortKey = savedInstanceState.getString(Extras.EXTRA_SORT_KEY);
			mSortOrder = savedInstanceState.getInt(Extras.EXTRA_SORT_ORDER);
		}

		boolean IsPickMultiple = false;
		int choiceMode = ListView.CHOICE_MODE_NONE;
		
		if (Intent.ACTION_PICK.equals(getIntent().getAction())) {
			setContentView(W.layout.entity_list_pick);
			if (mCanCreate) {
				View header = getLayoutInflater().inflate(
						android.R.layout.simple_list_item_1, getListView(),
						false);
				TextView text = (TextView) header.findViewById(android.R.id.text1);
				text.setText(W.string.insert_entity);
				getListView().addHeaderView(header);
			}

			if (getIntent().hasExtra(Extras.EXTRA_MULTIPLE)) {
				findViewById(W.id.footer).setVisibility(View.VISIBLE);
				findViewById(W.id.saveButton).setOnClickListener(this);
				findViewById(W.id.discardButton).setOnClickListener(this);
				
				IsPickMultiple = true;
				choiceMode = ListView.CHOICE_MODE_MULTIPLE;
			} else {
				choiceMode = ListView.CHOICE_MODE_SINGLE;
			}
		} else {
			setContentView(W.layout.entity_list_content);

			TextView empty = (TextView) findViewById(W.id.emptyText);
			empty.setText(W.string.noEntityHelpText);
		}
		
		mCursor = query();
		startManagingCursor(mCursor);
		
		if (mListAdapterFactory != null) {
			mAdapter = mListAdapterFactory.newListAdapter(this, mCursor, mColor, choiceMode);
		} else {
			mAdapter = new EntityCursorAdapter(this, mCursor, mColor, choiceMode);
		}
		setListAdapter(mAdapter);
		
		if (IsPickMultiple) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			if (getIntent().hasExtra(Extras.EXTRA_SELECTED)) {
				ArrayList<Uri> selected = getIntent().getParcelableArrayListExtra(Extras.EXTRA_SELECTED);
				ListView list = getListView();
				for (int i = 0; i < list.getCount(); i++) {
					long itemId = list.getItemIdAtPosition(i);
					Uri itemUri = ContentUris.withAppendedId(mUri, itemId);
					if (selected.contains(itemUri)) {
						list.setItemChecked(i, true);
					}
				}
			}
		}

		registerForContextMenu(getListView());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		IamLost.getInstance().remove();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(Extras.EXTRA_SORT_KEY, mSortKey);
		outState.putInt(Extras.EXTRA_SORT_ORDER, mSortOrder);
		outState.putParcelable(EXTRA_CURRENT_URI, mCurrentUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		mCurrentUri = state.getParcelable(EXTRA_CURRENT_URI);
	}
	
	@Override
	public void startActivity(Intent intent) {
		try {
			super.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			IntentUtils.treatException(e, this, intent);
		}
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		try {
			super.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			IntentUtils.treatException(e, this, intent);
		}
	}
	
	protected abstract void initWhereForSearch();

	protected abstract void init();

	private EntityCursor query() {
		String where = computeWhere(mSQLBuilder).toSQL();
		String order = mSortKey	+ (mSortOrder != SortOrder.DESCENDANT_ORDER	? " asc" : " desc");
		return (EntityCursor) SQLiteWrapper.query(this, mUri, where, order);
	}
	
	protected void requery() {
		stopManagingCursor(mCursor);
		mCursor = query();
		startManagingCursor(mCursor);
		mAdapter.changeCursor(mCursor);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position,
			long id) {
		super.onListItemClick(l, v, position, id);
		if (id < 0) {
			startActivityForResult(new Intent(Intent.ACTION_INSERT, mUri)
					.putExtras(getIntent()).addCategory(PreferenceHelper.getEditionCategory(this)),
					ACTIVITY_INSERT);
		} else {
			if (!getIntent().hasExtra(Extras.EXTRA_MULTIPLE)) {
				Uri uri = ContentUris.withAppendedId(mUri, id);
				if (Intent.ACTION_PICK.equals(getIntent().getAction())) {
					setResult(RESULT_OK, new Intent().setData(uri));
					finish();
				} else {
					startActivity(new Intent(Intent.ACTION_VIEW, uri));
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!Intent.ACTION_PICK.equals(getIntent().getAction()) && mCanCreate) {
			menu
					.add(Menu.NONE, MENU_NEW_ID, Menu.NONE, W.string.menu_new)
					.setIcon(android.R.drawable.ic_menu_add)
					.setIntent(
							new Intent(Intent.ACTION_INSERT, mUri)
									.addCategory(PreferenceHelper.getEditionCategory(this)));
		}

		menu.add(Menu.NONE, MENU_MARK_ALL_AS_READ_ID, Menu.NONE,
				W.string.menu_mark_all_as_read)
				.setIcon(W.drawable.ic_menu_mark);

		menu.add(Menu.NONE, MENU_IAMLOST_ID, 0, W.string.menu_iamlost).setIcon(
				android.R.drawable.ic_menu_myplaces);

		menu.add(Menu.NONE, MENU_SEARCH_ID, 0, android.R.string.search_go)
				.setIcon(android.R.drawable.ic_menu_search);
		
		addMapMenu(menu);

		SubMenu subMenu = menu.addSubMenu(W.string.menu_sort_by);
		subMenu.setIcon(android.R.drawable.ic_menu_sort_by_size);

		subMenu.add(MENU_SORT_BY_GROUP_ID, MENU_SORT_BY_MODIFIED_ID, Menu.NONE,
				W.string.menu_sort_by_modified);

		addSortMenuItems(MENU_SORT_BY_GROUP_ID, subMenu);

		subMenu.setGroupCheckable(MENU_SORT_BY_GROUP_ID, true, true);

		return super.onCreateOptionsMenu(menu);
	}
	
	protected abstract void addMapMenu(Menu menu);

	protected abstract void addSortMenuItems(int groupId, SubMenu subMenu);

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (Keys.KEY_MODIFIED.equals(mSortKey))
			menu.findItem(MENU_SORT_BY_MODIFIED_ID).setChecked(true);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_MARK_ALL_AS_READ_ID:
			DatabaseUtils.markAs(getContentResolver(), mUri, false);
			return true;
		case MENU_IAMLOST_ID:
			showDialog(DIALOG_IAMLOST_ID);
			return true;
		case MENU_SEARCH_ID:
			startSearch(null, false, null, false);
			return true;
		case MENU_SORT_BY_MODIFIED_ID:
			if (Keys.KEY_MODIFIED.equals(mSortKey)) {
				mSortOrder = mSortOrder != SortOrder.DESCENDANT_ORDER ? SortOrder.DESCENDANT_ORDER
						: SortOrder.ASCENDANT_ORDER;
			} else {
				mSortKey = Keys.KEY_MODIFIED;
				mSortOrder = SortOrder.DESCENDANT_ORDER;
			}
			requery();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		if (info.id < 0)
			return;

		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, CMENU_VIEW_ID, Menu.NONE, W.string.menu_view);
		if (mCanModify) {
			menu.add(Menu.NONE, CMENU_EDIT_ID, Menu.NONE, W.string.menu_edit);
		}
		if (mCanDelete) {
			menu.add(Menu.NONE, CMENU_DELETE_ID, Menu.NONE, W.string.menu_delete);
		}

		EntityCursor c = (EntityCursor) getListAdapter().getItem(
				info.position - getListView().getHeaderViewsCount());
		if (c.getUnread()) {
			menu.add(Menu.NONE, CMENU_MARK_AS_READ_ID, Menu.NONE,
					W.string.menu_mark_as_read);
		} else {
			menu.add(Menu.NONE, CMENU_MARK_AS_UNREAD_ID, Menu.NONE,
					W.string.menu_mark_as_unread);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Uri uri = ContentUris.withAppendedId(mUri, info.id);
		switch (item.getItemId()) {
		case CMENU_DELETE_ID:
			mCurrentUri = uri;
			showDialog(DIALOG_DELETE_ID);
			return true;
		case CMENU_EDIT_ID:
			startActivity(new Intent(Intent.ACTION_EDIT, uri));
			return true;
		case CMENU_VIEW_ID:
			startActivity(new Intent(Intent.ACTION_VIEW, uri));
			return true;
		case CMENU_MARK_AS_READ_ID:
			DatabaseUtils.markAs(getContentResolver(), uri, false);
			return true;
		case CMENU_MARK_AS_UNREAD_ID:
			DatabaseUtils.markAs(getContentResolver(), uri, true);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DELETE_ID:
			return new AlertDialog.Builder(this)
			.setTitle(W.string.delete_confirmation_title)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setMessage(W.string.delete_confirmation)
			.setNegativeButton(android.R.string.cancel, null)
			.setPositiveButton(android.R.string.ok, this)
			.setCancelable(false)
			.create();
		case DIALOG_IAMLOST_ID:
			return DialogFactory.createIamLostDialog(this);
		default:
			return super.onCreateDialog(id);
		}
	}
	
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			getContentResolver().delete(mCurrentUri, null, null);			
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == ACTIVITY_INSERT) {
			if (!getIntent().hasExtra(Extras.EXTRA_MULTIPLE)) {
				setResult(RESULT_OK, data);
				finish();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case W.id.saveButton:
			ListView list = getListView();
			SparseBooleanArray spa = list.getCheckedItemPositions();
			ArrayList<Uri> uris = new ArrayList<Uri>();
			if (spa.size() != 0) {
				for (int i = 0; i < list.getCount(); i++) {
					if (spa.get(i)) {
						long id = list.getItemIdAtPosition(i);
						if (id != -1)
							uris.add(ContentUris.withAppendedId(mUri, id));
					}
				}
			}
			Intent data = new Intent().putParcelableArrayListExtra(
					Extras.EXTRA_SELECTED, uris);
			setResult(RESULT_OK, data);
			finish();
			break;
		case W.id.discardButton:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}
}
