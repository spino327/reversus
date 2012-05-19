package com.lat.android;

import java.io.IOException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HistoryActivity extends LATActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        
        SharedPreferences prefs = this.getSharedPreferences("com.lat.android", MODE_PRIVATE);
    	final String url = prefs.getString("url", null) + "/history?";
    	String user = prefs.getString("user", "");
    	
    	TableLayout table = (TableLayout) findViewById(R.id.historyTable);
    	
    	HistoryDownloaderTask hdt = new HistoryDownloaderTask();
    	hdt.execute(url + "user=" + user, table);
    }
    
    private class HistoryDownloaderTask extends AsyncTask<Object, String, Boolean>{
    	//ListView list;
    	//ArrayList<String> places;
    	TableLayout table;
    	@Override
    	protected void onPreExecute(){
    		Log.i("My Debug Tag", "DL onPreExecute");
    		HistoryActivity.this.setProgressBarIndeterminateVisibility(true);
    	}
    	
    	protected void onPostExecute(Boolean result){
    		Log.i("My Debug Tag", "DL onPostExecute");
    		HistoryActivity.this.setProgressBarIndeterminateVisibility(false);
    		
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
                    processHistory(data);
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
        	if (values.length == 3){
        		insertHistoryRow(table, values[0],values[1],values[2]);
        		//Log.i("Note", values[0]);
        	}
        }
		
        private void processHistory(XmlPullParser data) throws XmlPullParserException, IOException {
            int eventType = -1;
            boolean bFoundData = false;

            // Find Score records from XML
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {

                    // Get the name of the tag (eg scores or score)
                    String strName = data.getName();

                    if (strName.equals("entry")) {
                        bFoundData = true;
                        String placeName = data.getAttributeValue(null, "name");
                        String date = data.getAttributeValue(null, "date");
                        String health = data.getAttributeValue(null, "health");
                        Log.i("Data", "place: " + placeName);
                        publishProgress(placeName, date, health);
                    }
                }
                eventType = data.next();
            }

            // Handle no scores available
            if (bFoundData == false) {
                publishProgress();
            }
        }
        
        private void insertHistoryRow(final TableLayout table, String place, String date, String health) {
            final TableRow newRow = new TableRow(HistoryActivity.this);

            int textColor = getResources().getColor(R.color.title_color);
            float textSize = getResources().getDimension(R.dimen.table_text_size);

            addTextToRowWithValues(newRow, place, textColor, textSize);
            addTextToRowWithValues(newRow, date, textColor, textSize);
            addTextToRowWithValues(newRow, health, textColor, textSize);
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
