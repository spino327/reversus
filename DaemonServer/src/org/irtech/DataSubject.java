package org.irtech;

import java.util.Map;

import org.agatha.db.ConnectTo;

/**
 * Intended to identify which class is the Subject in the Observer pattern
 * implemented for handling the Information Retrieval techniques
 * 
 * @author pinogal
 *
 */
public interface DataSubject {

	/**
	 * Add a new Observer to the List of observers, so that
	 * when an event occurred the DataSubject notifies it.
	 * @param obj
	 */
	void attach(DataObserver obj);
	
	/**
	 * Remove an Observer from the List of observers, so that
	 * when an event occurred this DataSubject is not longer notified.
	 * @param obj
	 */
	void detach(DataObserver obj);
	
	/**
	 * This method should go thought the list of Observers and call computeIRTech method
	 * for each one.
	 * 
	 * @param data A Map with pairs (key, value) related to latitude, longitude, timespan, user id, and so on.
	 * @param conn ConnectTo object that represent the established database connection
	 */
	void notifyObservers(Map<String, String> data, ConnectTo conn);
}
