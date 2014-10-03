package com.team9.healthmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.team9.healthmate.R;

public class Login extends Activity implements OnClickListener{
	public Button enter; 
	/*Please don't not delete delete this button until full
	system integration */
	public Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		
		
		enter = (Button) findViewById(R.id.enter_button);
		enter.setOnClickListener(this);
		
	}
	public void startAct()	{
		intent = new Intent(Login.this, Menu.class);
		startActivity(intent);
	}
	@Override
	public void onClick(View v) {
		startAct();
		
	}
	
}
