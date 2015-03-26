package test.java.org.hanzet23.gameframework;

import main.java.org.hanzet23.gameframework.models.Network;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for the Network class
 */
public class NetworkTest extends TestCase {

	public NetworkTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(NetworkTest.class);
	}
	
	public void testMakingTheConnection() {
		Network network = new Network(0, null);
		
		// Network object shouldn't be empty
		assertNotNull(network);
		
		// Socket object shouldn't be empty
		assertNotNull(network.getSocket());
		
		// PrintWriter object shouldn't be empty
		assertNotNull(network.getOutput());
		
		// BufferedReader object shouldn't be empty
		assertNotNull(network.getInput());
	}
}