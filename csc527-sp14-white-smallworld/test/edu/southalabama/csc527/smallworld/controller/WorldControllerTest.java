package edu.southalabama.csc527.smallworld.controller;

import junit.framework.TestCase;
import edu.southalabama.csc527.smallworld.TestConstants;
import edu.southalabama.csc527.smallworld.controller.WorldController;
import edu.southalabama.csc527.smallworld.model.Direction;
import edu.southalabama.csc527.smallworld.model.IWorldObserver;
import edu.southalabama.csc527.smallworld.model.Player;
import edu.southalabama.csc527.smallworld.model.World;

public class WorldControllerTest extends TestCase {

	private WorldController f_wc;

	private boolean f_observed;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		f_wc = new WorldController(TestConstants.TESTFILE);
		IWorldObserver observer = new IWorldObserver() {
			public void update(World world) {
				f_observed = true;
			}
		};
		f_wc.getWorld().addObserver(observer);
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.controller.WorldController.WorldController()'
	 */
	public void testWorldController() {
		f_wc = new WorldController();
		assertNotNull(f_wc.getWorld());
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.controller.WorldController.WorldController(String)'
	 */
	public void testWorldControllerString() {
		// Just getting through setUp() is enough!
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.controller.WorldController.getWorld()'
	 */
	public void testGetWorld() {
		assertNotNull(f_wc.getWorld());
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.controller.WorldController.travel(Direction)'
	 */
	public void testTravel() {
		World w = f_wc.getWorld();
		Player p = w.getPlayer();
		f_observed = false;
		assertEquals(w.getPlace("Living Room"), p.getLocation());
		f_wc.travel(Direction.NORTH);
		assertEquals(w.getPlace("Hall"), p.getLocation());
		assertTrue(f_observed);
		f_wc.travel(Direction.NORTH);
		assertEquals(w.getPlace("Bathroom"), p.getLocation());
		f_wc.travel(Direction.NORTH);
		assertEquals(w.getPlace("Bathroom"), p.getLocation());
		try {
			f_wc.travel(null); // should throw AssertionError
		} catch (AssertionError e) {
		}
		assertEquals(w.getPlace("Bathroom"), p.getLocation());
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.controller.WorldController.quit()'
	 */
	public void testQuit() {
		f_observed = false;
		assertFalse(f_wc.getWorld().isGameOver());
		f_wc.quit();
		assertTrue(f_wc.getWorld().isGameOver());
		assertTrue(f_observed);
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.controller.WorldController.saveWorld(String)'
	 * and
	 * 'edu.southalabama.csc527.smallworld.controller.WorldController.loadWorld(String)'
	 */
	public void testLoadSaveWorld() {
		f_wc.saveWorld(TestConstants.SAVEFILE);
		f_wc.loadWorld(TestConstants.SAVEFILE);
		assertEquals(f_wc.getWorld().getPlace("Living Room"), f_wc.getWorld()
				.getPlayer().getLocation());
		try {
			f_wc.saveWorld(null); // should throw AssertionError
			fail();
		} catch (AssertionError e) {
		}
		try {
			f_wc.loadWorld(null); // should throw AssertionError
			fail();
		} catch (AssertionError e) {
		}
	}
}
