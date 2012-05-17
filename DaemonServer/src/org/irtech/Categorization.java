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

import java.util.Map;

import org.db.DBHandler;
import org.irtech.categorization.LanguageModel;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.Category;
import fi.foyt.foursquare.api.entities.CompleteVenue;

/**
 * Perform categorization
 * 
 * @author pinogal
 *
 */
public class Categorization implements DataObserver {

	private LanguageModel lm;
	private static final FoursquareApi foursquareApi = 
			new FoursquareApi("VRPY4TXFP4WT3JUI5EJAEKQUKFLX0IS1CD3E5TKJN52HYN24",
			"Q1AQQ1IZAJLJITYU5DDGIBRJVOUWS3C2AVOTDJWVMMZTDIPH",
			"http://osroboticsalliance.com");
	
	public Categorization() {
//		lm = new LanguageModel("data/homegrown.txt", 
//					"data/kates.txt");
		lm = new LanguageModel("data/healthy.txt", 
					"data/unhealthy.txt");
	}
	
	@Override
	public void computeIRTech(Map<String, String> data, DBHandler db) {

		if (data.containsKey("placeID")) {

			int placeID = Integer.parseInt(data.get("placeID"));

			if (!db.hasCategory(placeID)) {

				String fsPlaceID = db.getPlaceFSID(placeID);

				if (fsPlaceID != null) {
					
					// connect to foursquare to get description or menu of the place
					
					Result<CompleteVenue> result = null;
					try {
						result = foursquareApi.venue(fsPlaceID);
						
					} catch (FoursquareApiException e) {
						e.printStackTrace();
						return;
					}

					if (result.getMeta().getCode() == 200) {

						StringBuffer sb = new StringBuffer();
						
						String tmp = result.getResult().getDescription();
						if (tmp != null)
							sb.append(tmp);
						
						for (Category cat : result.getResult().getCategories()) {
							sb.append(" ");
							tmp = cat.getName();
							
							if (tmp != null)
								sb.append(tmp);
						}
						
						for(String ele : result.getResult().getTags()) {
							sb.append(" ");
							sb.append(ele);
						}
						
						System.out.println("Test text: " + sb);
						
						// it is a vector [category, naive bayes, relative entropy]
						Object[] res = lm.getClassification(sb.toString());

						System.out.println(res[0] + ", naive bayes: " + res[1] + ", relative entropy: " + res[2]);

						//add place category
						db.addPlaceCategory(placeID, (String)res[0]);
					}

				}

			}

		}
	}

}
