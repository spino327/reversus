package com.lat.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends LATActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        TextView regLink = (TextView) findViewById(R.id.link_to_register);
        
        regLink.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(i);
            }
        });
        
    }
    
    public void login(View view){
    	SharedPreferences prefs = this.getSharedPreferences("com.lat.android", MODE_PRIVATE);
    	EditText username = (EditText) findViewById(R.id.editTextUsername);
    	
    	if (!username.getText().toString().equals("")){
    		prefs.edit().putString("user", username.getText().toString()).commit();
    	
    		Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
    		startActivity(i);
    	}
    	else{
    		username.setText("Please input a username");
    	}
    }
}
