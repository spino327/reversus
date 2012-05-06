package org.irtech;

import java.util.Map;
import org.agatha.db.ConnectTo;

/**
 * 
 * Used for the Observer like design pattern implemented for handling the implementation 
 * of the Information Retrieval techniques
 * 
 * @author pinogal
 */
public interface DataObserver {

	void computeIRTech(Map<String, String> data, ConnectTo conn);
	
}
