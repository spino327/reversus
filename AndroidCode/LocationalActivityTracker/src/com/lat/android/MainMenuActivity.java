package com.lat.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

public class MainMenuActivity extends LATActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        ListView menuList = (ListView) findViewById(R.id.ListView_Menu);
        
        String[] items = {
        		getResources().getString(R.string.check_in_title),
        		getResources().getString(R.string.history_title),
        		getResources().getString(R.string.my_alerts_title),
        		getResources().getString(R.string.statistics_title),
        		getResources().getString(R.string.settings_title)
        };
        
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.menu_item, items);
        
        menuList.setAdapter(adapt);
        
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id){
        		TextView textView = (TextView) itemClicked;
        		String strText = textView.getText().toString();
        		if (strText.equalsIgnoreCase(getResources().getString(R.string.check_in_title))){
        			startActivity(new Intent(MainMenuActivity.this, CheckInActivity.class));
        		}
        		else if (strText.equalsIgnoreCase(getResources().getString(R.string.history_title))){
        			startActivity(new Intent(MainMenuActivity.this, HistoryActivity.class));
        		}
        		else if (strText.equalsIgnoreCase(getResources().getString(R.string.my_alerts_title))){
        			startActivity(new Intent(MainMenuActivity.this, MyAlertsActivity.class));
        		}
        		else if (strText.equalsIgnoreCase(getResources().getString(R.string.statistics_title))){
        			startActivity(new Intent(MainMenuActivity.this, StatisticsActivity.class));
        		}
        		else if (strText.equalsIgnoreCase(getResources().getString(R.string.settings_title))){
        			startActivity(new Intent(MainMenuActivity.this, SettingsActivity.class));
        		}
        	}
		});
        
        SharedPreferences prefs = this.getSharedPreferences("com.lat.android", Context.MODE_PRIVATE);
        prefs.edit().putString("url", "http://192.168.2.104:8080/LATServlet").commit();
    }
}
