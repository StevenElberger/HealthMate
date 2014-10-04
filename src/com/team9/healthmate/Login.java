package com.team9.healthmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.team9.healthmate.R;

/** 
 * Presents the login screen to users. Also allows
 * new users to register for an account.
 * @author Steve
 * 
 */
public class Login extends Activity implements OnClickListener{
	/*Please don't not delete delete this button until full
	system integration */
	public Button login;
	public Button register;
	//public Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		login = (Button) findViewById(R.id.login_button);
		login.setOnClickListener(this);
		
		register = (Button) findViewById(R.id.register_button);
		register.setOnClickListener(this);
	}
	
	public void startMenu()	{
		Intent intent = new Intent(Login.this, Menu.class);
		startActivity(intent);
	}
	
	public void startRegistration()	{
		Intent intent = new Intent(Login.this, Registration.class);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		if (v.equals(register)) {
			startRegistration();
		} else if (v.equals(login)) {
			startMenu();
		}
	}	
}