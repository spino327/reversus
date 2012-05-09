


import java.util.ArrayList;

import fi.foyt.foursquare.api.*;
import fi.foyt.foursquare.api.entities.*;
//import fi.foyt.foursquare.api.entities.VenuesSearchResult;

public class LocationAnalyzer {
	public static FoursquareApi foursquareApi;
	
	LocationAnalyzer(){
		foursquareApi = new FoursquareApi("VRPY4TXFP4WT3JUI5EJAEKQUKFLX0IS1CD3E5TKJN52HYN24",
				"Q1AQQ1IZAJLJITYU5DDGIBRJVOUWS3C2AVOTDJWVMMZTDIPH",
				"http://osroboticsalliance.com");
	}
	
	public ArrayList<String> bestResult(String lat, String lon){
		ArrayList<String> bestResults = new ArrayList<String>();
		Result<VenuesSearchResult> result = null;
		try {
			result = foursquareApi.venuesSearch(lat + "," + lon, null, null, null, null, null, null, null, null, null, null);
		} catch (FoursquareApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    if (result.getMeta().getCode() == 200) {
	    	System.out.println("Possible Locations:");
	        // if query was ok we can finally we do something with the data
	        for (CompactVenue venue : result.getResult().getVenues()) {
	          for (Category cat : venue.getCategories()){
	        	  for (String par : cat.getParents()){
	        		  if (par.equals("Food")){
	        			  System.out.println(venue.getName());
	        			  bestResults.add(venue.getName());
	        		  }
	        	  }
	          }
	        }
	      } else {
	        // TODO: Proper error handling
	        System.out.println("Error occured: ");
	        System.out.println("  code: " + result.getMeta().getCode());
	        System.out.println("  type: " + result.getMeta().getErrorType());
	        System.out.println("  detail: " + result.getMeta().getErrorDetail()); 
	      }
	    return bestResults;
	}
}
