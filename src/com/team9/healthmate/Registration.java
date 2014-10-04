package com.team9.healthmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;

/** 
 * Presents the registration screen to users. Allows
 * users to create a new account.
 * @author Steve
 * 
 */
public class Registration extends Activity {
	//public Button createAccount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		//createAccount = (Button) findViewById(R.id.register_button);
		//createAccount.setOnClickListener(this);
	}

	public void startAct()	{
		Intent intent = new Intent(Registration.this, Menu.class);
		startActivity(intent);
	}
	
/*	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		startAct();
	}*/
}
