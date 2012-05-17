/**
 * Copyright (c) 2012, University of Delaware
 * All rights reserved.
 *
 * @author: Sergio Pino
 * @author: Keith Elliott
 * Website: http://www.eecis.udel.edu/~pinogal, http://www.eecis.udel.edu/~kelliott
 * emails  : sergiop@udel.edu - kelliott@udel.edu
 * Date   : May, 2012
 *
 */

package org.irtech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.db.DBHandler;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;

/**
 * Access the foursquared API and use 
 * @author pinogal
 *
 */
public class PlaceResolution {

	private static final FoursquareApi foursquareApi = 
			new FoursquareApi("VRPY4TXFP4WT3JUI5EJAEKQUKFLX0IS1CD3E5TKJN52HYN24",
			"Q1AQQ1IZAJLJITYU5DDGIBRJVOUWS3C2AVOTDJWVMMZTDIPH",
			"http://osroboticsalliance.com");
	
	/**
	 * returns the placeID that represents the most likely place
	 * in which the user checked in. We use the distance of the place(returned by foursquare API)
	 * and the p(li|Uj) = Probability of the Place given the user, in order to compute the most likely place.
	 * 
	 * @param data Map with user, lat, lon, ts
	 * @param dbHandler 
	 * @return placeID
	 */
	public static int getResolution(Map<String, String> data, DBHandler dbHandler) {
		
		String lat = data.get("lat");
		String lon = data.get("lon");
		int userId = Integer.parseInt(data.get("user"));

		int placeID = -1;
		
		// Get a DescriptiveStatistics instance
		DescriptiveStatistics stats = new DescriptiveStatistics();

		Result<VenuesSearchResult> result = null;
		try {
			result = foursquareApi.venuesSearch(lat + "," + lon, null, null, null, null, null, null, null, null, null, null);
		} catch (FoursquareApiException e) {
			e.printStackTrace();
		}

		if (result.getMeta().getCode() == 200) {
			
			// matrix of [placeID, c(li,Uj), distance]
			Object[][] places = new Object[result.getResult().getVenues().length][3];
			
			System.out.println("Possible Locations:");
			
			int count = 0;
			// if query was ok we can finally we do something with the data
			for (CompactVenue venue : result.getResult().getVenues()) {
				//for (Category cat : venue.getCategories()){
					//for (String par : cat.getParents()){
						//if (par.equals("Food")){
				float dis = venue.getLocation().getDistance().floatValue();
				String placeName = venue.getName();
				
				System.out.println(placeName + ", " + dis);
				
				int pId = dbHandler.getPlaceId(placeName, 
						venue.getLocation().getLat().floatValue(), 
						venue.getLocation().getLng().floatValue(),
						venue.getId());
				
				int c_li_uj = dbHandler.getHaveBeenEntry(userId, pId);
				
				places[count][0] = pId;
				places[count][1] = c_li_uj;
				places[count][2] = dis;
				
				count++;
						//}
					//}
				//}
			}
			
			// computing 

			float sum_c_li_uj = dbHandler.getSumHaveBeenEntry(userId);
			float numPlaces = dbHandler.getTotalNumPlaces();
			Map<Float, Integer> pres = new HashMap<Float, Integer>();
			
			for (int i = 0; i < count; i++) {
				
				// p(li | Uj) = ( c(l_i, U_j) + 1 ) / (sum(c(l_i, U_j)) + |V|)
				// using matrix of [placeID, c(li,Uj), distance]
				float p_li_uj =  ( (Integer)places[i][1] + 1 ) / ( sum_c_li_uj + numPlaces );
				// algorithm (1/distance)*p(li | Uj)
				float pRWeight = (1/((Float)places[i][2])) * p_li_uj;
				
				pres.put( pRWeight,  (Integer)places[i][0]);
				
				stats.addValue(pRWeight);
			}
			
			// finding the maximum value
			float max = (float) stats.getMax();
			
			return pres.get(max);
			
		} else {
			// TODO: Proper error handling
			System.out.println("Error occured: ");
			System.out.println("  code: " + result.getMeta().getCode());
			System.out.println("  type: " + result.getMeta().getErrorType());
			System.out.println("  detail: " + result.getMeta().getErrorDetail());
		}
	    
	    
	    return placeID;
	}
	
}
