package com.fs_server;

import fi.foyt.foursquare.api.*;
import fi.foyt.foursquare.api.entities.*;
//import fi.foyt.foursquare.api.entities.VenuesSearchResult;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws FoursquareApiException{
		FoursquareApi foursquareApi = new FoursquareApi("VRPY4TXFP4WT3JUI5EJAEKQUKFLX0IS1CD3E5TKJN52HYN24",
														"Q1AQQ1IZAJLJITYU5DDGIBRJVOUWS3C2AVOTDJWVMMZTDIPH",
														"http://osroboticsalliance.com");
		Result<VenuesSearchResult> result = null;
		try {
			result = foursquareApi.venuesSearch("39.683366,-75.7476", null, null, null, null, null, null, null, null, null, null);
		} catch (FoursquareApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    if (result.getMeta().getCode() == 200) {
	        // if query was ok we can finally we do something with the data
	        for (CompactVenue venue : result.getResult().getVenues()) {
	          for (Category cat : venue.getCategories()){
	        	  for (String par : cat.getParents())
	        		  if (par.equals("Food"))
	        			  System.out.println(venue.getName());
	          }
	        }
	      } else {
	        // TODO: Proper error handling
	        System.out.println("Error occured: ");
	        System.out.println("  code: " + result.getMeta().getCode());
	        System.out.println("  type: " + result.getMeta().getErrorType());
	        System.out.println("  detail: " + result.getMeta().getErrorDetail()); 
	      }
	}

}
