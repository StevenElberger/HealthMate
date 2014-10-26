package com.team9.healthmate;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;;


public class MoodsDetail {
	

	public boolean formIsValid(View layout) {
	    for (int i = 0; i < ((ViewGroup) layout).getChildCount(); i++) {
	        View v = ((ViewGroup) layout).getChildAt(i);
	        if (v instanceof EditText) {
	            //validate your EditText here
	        } else if (v instanceof RadioButton) {
	            //validate RadioButton
	        } //etc. If it fails anywhere, just return false.
	    }
	    return true;
	}
}
