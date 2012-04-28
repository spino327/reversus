package org.net;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.agatha.db.ConnectTo;
import org.irtech.DataObserver;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Test_ServerInterface {

	private static ServerInterface server;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		server = new ServerInterface(8080);
		
		
		// registering Information Retrieval Observers
		server.attach(new DataObserver() {
			
			@Override
			public void computeIRTech(Map<String, String> data, ConnectTo conn) {
				
				System.out.println("Processing IR technique 1");
				
				for (Map.Entry<String, String> entry : data.entrySet()) {
					System.out.println(entry);
				}
				
				System.out.println(conn);
				
				System.out.println("EO Processing IR technique 1");
			}
		});
		
		
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
		
		URL url = new URL("http://localhost:8080/gateway");
		URLConnection connection = url.openConnection();
		
		connection.setRequestProperty("lat", Float.toString((float) 39.6837226));
		connection.setRequestProperty("lon", Float.toString((float) -75.7496572));
		connection.setRequestProperty("user", Integer.toString(0));
		connection.setRequestProperty("ts", (new Date()).toString());
		
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
