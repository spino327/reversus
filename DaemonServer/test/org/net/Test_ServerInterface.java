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

package org.net;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.irtech.Categorization;
import org.irtech.DataObserver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.db.*;

public class Test_ServerInterface {

	private static ServerInterface server;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		server = new ServerInterface(8080);
		
		
		// registering Information Retrieval Observers
//		server.attach(new DataObserver() {
//			
//			@Override
//			public void computeIRTech(Map<String, String> data, DBHandler db) {
//				
//				System.out.println("Processing IR technique 1");
//				
//				for (Map.Entry<String, String> entry : data.entrySet()) {
//					System.out.println(entry);
//				}
//				
//				System.out.println(db);
//				
//				System.out.println("EO Processing IR technique 1");
//			}
//		});
		
		server.attach(new Categorization());
		
		server.start();
//		server.join();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		server.stop();
	}

//	@Before
//	public void setUp() throws Exception {
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		
//	}

	@Test
	public void testConnection() throws IOException {
		
		long ts = new Date().getTime();

//		URL url = new URL("http://localhost:8080/gateway?lat=39.683366&lon=-75.7476&user=1&ts="+ts);//cheeburger
//		URL url = new URL("http://localhost:8080/gateway?lat=39.683353&lon=-75.74858&user=1&ts="+ts);//cosi
//		URL url = new URL("http://localhost:8080/gateway?lat=39.683483&lon=-75.74768&user=1&ts="+ts);//margueritas pizza
//		URL url = new URL("http://localhost:8080/gateway?lat=39.683629&lon=-75.746323&user=1&ts="+ts);//PitaPit
		URL url = new URL("http://localhost:8080/gateway?lat=39.6837&lon=-75.74754&user=1&ts="+ts);//Panera

		URLConnection connection = url.openConnection();
		
//		connection.setRequestProperty("lat", Float.toString((float) 39.6837226));
//		connection.setRequestProperty("lon", Float.toString((float) -75.7496572));
//		connection.setRequestProperty("user", Integer.toString(0));
//		connection.setRequestProperty("ts", (new Date()).toString());
		
		connection.connect();
		
		Map<String, List<String>> headers = connection.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			String key = entry.getKey();
			for (String value : entry.getValue())
				System.out.println(key + ": " + value);
		}
		
		boolean res = Boolean.parseBoolean(connection.getHeaderField("Result"));
		
		assertTrue(res);
	}

}
