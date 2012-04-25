package org.net;

import static org.junit.Assert.*;

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
		server.join();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void testConnection() {
		
	}

}
