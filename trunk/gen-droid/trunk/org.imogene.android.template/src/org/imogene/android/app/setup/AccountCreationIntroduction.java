package org.imogene.android.app.setup;

import org.imogene.android.W;
import org.imogene.android.app.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AccountCreationIntroduction extends BaseActivity implements OnClickListener{
	
	private Button mNextButton;
	
	public static final void accountCreationIntroduction(Activity fromActivity) {
		Intent i = new Intent(fromActivity, AccountCreationIntroduction.class);
		fromActivity.startActivity(i);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(W.layout.account_creation_introduction);
		
		mNextButton = (Button) findViewById(W.id.next);
		
		mNextButton.setOnClickListener(this);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		getActivityHelper().setActionBarClickable(false);
	}
	
	public void onClick(View v) {
		AccountSetupBasics.actionNewAccount(this);
		finish();
	}

}
