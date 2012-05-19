package com.lat.android;

import java.io.IOException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MyAlertsActivity extends LATActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alerts);
        
        TableLayout table = (TableLayout) findViewById(R.id.alertTable);
        
        AlertDownloaderTask adt = new AlertDownloaderTask();
        
        SharedPreferences prefs = this.getSharedPreferences("com.lat.android", MODE_PRIVATE);
    	final String url = prefs.getString("url", null) + "/alerts?";
    	String user = prefs.getString("user", "");
    	
    	String params = "&";
    	
    	params += "get=yes";
        
        adt.execute(url + "user=" + user + params, table);
    }
    
    public void newAlert(View view){
    	Intent i = new Intent(getApplicationContext(), NewAlertActivity.class);
    	startActivity(i);
    }
    
    private class AlertDownloaderTask extends AsyncTask<Object, String, Boolean>{
    	//ListView list;
    	//ArrayList<String> places;
    	TableLayout table;
    	@Override
    	protected void onPreExecute(){
    		Log.i("My Debug Tag", "DL onPreExecute");
    		MyAlertsActivity.this.setProgressBarIndeterminateVisibility(true);
    	}
    	
    	protected void onPostExecute(Boolean result){
    		Log.i("My Debug Tag", "DL onPostExecute");
    		MyAlertsActivity.this.setProgressBarIndeterminateVisibility(false);
    		
    	}
    	
		@Override
		protected Boolean doInBackground(Object... params) {
			boolean result = false;
			
            String pathToData =  (String) params[0];
            table = (TableLayout) params[1];
            
            XmlPullParser data;
            try {
            	Log.i("Comment", "Connecting to " + pathToData);
            	
                URL xmlUrl = new URL(pathToData);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                data = factory.newPullParser();
                data.setInput(xmlUrl.openStream(), null);
            } catch (XmlPullParserException e) {
                data = null;
                e.printStackTrace();
            } catch (IOException e) {
                data = null;
                e.printStackTrace();
            }

            if (data != null) {
                try {
                	Log.i("Note", "Processing XML");
                    processAlerts(data);
                } catch (XmlPullParserException e) {
                    Log.e("My Debug Tag", "Pull Parser failure", e);
                } catch (IOException e) {
                    Log.e("My Debug Tag", "IO Exception parsing XML", e);
                }
            }
			return result;
		}

        @Override
        protected void onProgressUpdate(String... values) {
        	if (values.length == 2){
        		insertAlertRow(table, values[0],values[1]);
        		//Log.i("Note", values[0]);
        	}
        }
		
        private void processAlerts(XmlPullParser data) throws XmlPullParserException, IOException {
            int eventType = -1;
            boolean bFoundData = false;

            // Find Score records from XML
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {

                    // Get the name of the tag (eg scores or score)
                    String strName = data.getName();

                    if (strName.equals("alert")) {
                        bFoundData = true;
                        String message = data.getAttributeValue(null, "message");
                        String date = data.getAttributeValue(null, "date");
                        Log.i("Data", "place: " + message);
                        publishProgress(message, date);
                    }
                }
                eventType = data.next();
            }

            // Handle no scores available
            if (bFoundData == false) {
                publishProgress();
            }
        }
        
        private void insertAlertRow(final TableLayout table, String message, String date) {
            final TableRow newRow = new TableRow(MyAlertsActivity.this);

            int textColor = getResources().getColor(R.color.title_color);
            float textSize = getResources().getDimension(R.dimen.table_text_size);

            addTextToRowWithValues(newRow, message, textColor, textSize);
            addTextToRowWithValues(newRow, date, textColor, textSize);
            table.addView(newRow);
        }
    }
    
    private void addTextToRowWithValues(final TableRow tableRow, String text, int textColor, float textSize) {
        TextView textView = new TextView(this);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setText(text);
        tableRow.addView(textView);
    }

}
