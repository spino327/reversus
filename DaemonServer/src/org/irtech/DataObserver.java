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

/**
 * 
 * Used for the Observer like design pattern implemented for handling the implementation 
 * of the Information Retrieval techniques
 * 
 * @author pinogal
 */
public interface DataObserver {

	/**
	 * 
	 * @param data ((lat: ); (lon: ); (user: );(ts:))
	 * @param conn conn to the dtabase
	 */
	void computeIRTech(Map<String, String> data, DBHandler db);
	
}
