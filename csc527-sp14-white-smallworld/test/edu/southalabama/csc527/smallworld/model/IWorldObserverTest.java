package edu.southalabama.csc527.smallworld.model;

import edu.southalabama.csc527.smallworld.model.IWorldObserver;
import edu.southalabama.csc527.smallworld.model.World;
import junit.framework.TestCase;

public class IWorldObserverTest extends TestCase implements IWorldObserver {

	private String message;

	public void testObserver() {
		World w = new World(); // any old world

		message = "Hello";
		w.setMessage(message);
		w.turnOver();

		// the sendMessage should reset the world message to ""
		message = "";
		w.turnOver();

		message = "Goodbye";
		w.setMessage(message);
		w.turnOver();
	}

	public void update(World world) {
		if (!world.getMessage().equals(message))
			fail("strange message: got \"" + world.getMessage()
					+ "\" expected\"" + message + "\"");
	}
}
