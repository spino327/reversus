package com.lat.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CheckInActivity extends LATActivity implements LocationListener{
    /** Called when the activity is first created. */

    double lat = 0, lon = 0;
    long timespan = 0;
    LocationManager locMgr;
    public static Context context;
    public ArrayList<String> places;
    public ArrayAdapter aa;
    public ListView mainList;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin);
        locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ListView list = (ListView) findViewById(R.id.possiblePlacesList);
        context = this.getApplicationContext();
        mainList = list;

		places = new ArrayList<String>();
		aa = new ArrayAdapter<String>(this, R.layout.list_item, places);
		list.setAdapter(aa);
    }
    
    public void updateCheckIns(View view){
        try {
        	
            Location recentLoc = locMgr.getLastKnownLocation(locMgr.getBestProvider(new Criteria(), true));
            if (recentLoc != null){
            	lat = (double) recentLoc.getLatitude();
            	lon = (double) recentLoc.getLongitude();
            	timespan = recentLoc.getTime();
            	
            }
        } catch (Exception e) {
            Log.e("My Debug Tag", "Location failed", e);
        }
    	
    	TextView latText = (TextView) findViewById(R.id.textViewLat);
    	TextView lonText = (TextView) findViewById(R.id.textViewLon);
    	TextView timeText = (TextView) findViewById(R.id.textViewTime);
    	if (lat != 0.0 && lon != 0.0){
    		latText.setText("Lat: " + lat);
    		lonText.setText("Long: " + lon);
    		timeText.setText("Time: " + timespan);
    		
    		sendCheckIn();
    		
    	}
    	else{
    		latText.setText("No Location Data Available");
    	}
    	
    }
    
    private class LocationUploaderTask extends AsyncTask<String, String, Boolean>{
    	@Override
    	protected void onPreExecute(){
    		Log.i("My Debug Tag", "UL onPreExecute");
    		CheckInActivity.this.setProgressBarIndeterminateVisibility(true);
    	}
    	
    	protected void onPostExecute(Boolean result){
    		Log.i("My Debug Tag", "UL onPostExecute");
    		
    		CheckInActivity.this.setProgressBarIndeterminateVisibility(false);
    	}

    	@Override
    	protected Boolean doInBackground(String... params) {
    		boolean result = false;
    		// TODO Auto-generated method stub
    		
            HttpGet request = new HttpGet(params[0] + params[1]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpClient client = new DefaultHttpClient();
            String responseBody = "Hello";
            try {
    			responseBody = client.execute(request, responseHandler);
    		} catch (ClientProtocolException e) {
    			// TODO Auto-generated catch block
    			Log.e("My Debug Tag", "Client Protocol Exception");
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			Log.e("My Debug Tag", "IO exception");
    		} catch (Exception e){
    			Log.e("My Debug Tag", responseBody);
    		}
			return result;
    	}
    }
    
    private class PossibilityDownloaderTask extends AsyncTask<Object, String, Boolean>{
    	//ListView list;
    	//ArrayList<String> places;
    	@Override
    	protected void onPreExecute(){
    		Log.i("My Debug Tag", "DL onPreExecute");
    		CheckInActivity.this.setProgressBarIndeterminateVisibility(true);
    		places.clear();
    	}
    	
    	protected void onPostExecute(Boolean result){
    		Log.i("My Debug Tag", "DL onPostExecute");
    		
    		CheckInActivity.this.setProgressBarIndeterminateVisibility(false);
    		
    	}
    	
		@Override
		protected Boolean doInBackground(Object... params) {
			// TODO Auto-generated method stub
			boolean result = false;
			
            String pathToData =  (String) params[0];
            //list = (ListView) params[1];
            //places = new ArrayList<String>();

            //HttpGet request = new HttpGet(pathToData);
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            //HttpClient client = new DefaultHttpClient();
            
            //String responseBody = "";
            XmlPullParser data;
            try {
            	Log.i("Comment", "Connecting to " + pathToData);
            	
            	//responseBody = client.execute(request, responseHandler);
            	
                URL xmlUrl = new URL(pathToData);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                //factory.setNamespaceAware(true);
                data = factory.newPullParser();
                //data.setInput(new StringReader(responseBody));
                data.setInput(xmlUrl.openStream(), null);
                //data.setInput((InputStream)xmlUrl.getContent(), "utf-8");
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
                    processPossibilites(data);
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
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (values.length == 1) {
                String placeName = values[0];
                places.add(placeName);
                //TextView tv = new TextView(context);
                //tv.setText(placeName);
                //mainList.addView(tv);
            } else {
                places.add("No results found");
            }
            
            aa.notifyDataSetChanged();
        }
		
        private void processPossibilites(XmlPullParser data) throws XmlPullParserException, IOException {
            int eventType = -1;
            boolean bFoundData = false;

            // Find Score records from XML
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {

                    // Get the name of the tag (eg scores or score)
                    String strName = data.getName();

                    if (strName.equals("possible")) {
                        bFoundData = true;
                        String placeName = data.getAttributeValue(null, "name");
                        Log.i("Data", placeName);
                        publishProgress(placeName);
                    }
                }
                eventType = data.next();
            }

            // Handle no scores available
            if (bFoundData == false) {
                publishProgress();
            }
        }
    	
    }

    public void sendCheckIn(){
    	SharedPreferences prefs = this.getSharedPreferences("com.lat.android", MODE_PRIVATE);
    	String url = prefs.getString("url", null);
    	if (url == null){
    		Log.e("My Debug Tag", "No url set!");
    		return;
    	}
    	url += "/checkin?";
//    	if(!url.endsWith("?")){
//            url += "?";
//    	}
    	
    	//LocationUploaderTask locUploader = new LocationUploaderTask();
    	//locUploader.execute(url);
    	PossibilityDownloaderTask pdl = new PossibilityDownloaderTask();
    	
    	List <NameValuePair> params = new LinkedList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair("lat", String.valueOf(lat)));
        params.add(new BasicNameValuePair("lon", String.valueOf(lon)));
        params.add(new BasicNameValuePair("user", prefs.getString("user", "guest")));
        params.add(new BasicNameValuePair("ts", String.valueOf(timespan)));
        
        String paramString = URLEncodedUtils.format(params, null);
        
        //locUploader.execute(url, paramString);
        pdl.execute(url + paramString, findViewById(R.id.possiblePlacesList));
        ListView list = (ListView) findViewById(R.id.possiblePlacesList);
        
        /*
        list.setVisibility(list.GONE);
        list.setAdapter(new ArrayAdapter<String>(this, R.layout.menu_item, places));
        
        list.setVisibility(list.VISIBLE);
        */
        //aa.notifyDataSetChanged();
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		String prov = locMgr.getBestProvider(new Criteria(), true);
		if (prov != null)
			locMgr.requestLocationUpdates(prov, 20000, 1, this);
	}

	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locMgr.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location loc) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
