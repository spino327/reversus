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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Test_ServerInterface {

	private static ServerInterface server;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		ServerInterface server = new ServerInterface(8080);
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
		connection.setRequestProperty("user", Integer.toString(10));
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
