package edu.southalabama.csc527.smallworld.model;

import java.util.Set;

import edu.southalabama.csc527.smallworld.model.IWorldObserver;
import edu.southalabama.csc527.smallworld.model.Place;
import edu.southalabama.csc527.smallworld.model.World;

import junit.framework.TestCase;

/**
 * @see World
 */
public class WorldTest extends TestCase {

	private World f_world;

	private boolean f_observed;

	@Override
	protected void setUp() {
		f_world = new World();
	}

	public void testWorld() {
		assertEquals(1, f_world.getPlaces().size());
		assertNotNull(f_world.getNowherePlace());
		assertNotNull(f_world.getPlayer());
		try {
			f_world.getPlace(null);
			fail();
		} catch (AssertionError e) {
		}
		try {
			f_world.getPlaceByName(null);
			fail();
		} catch (AssertionError e) {
		}
		try {
			f_world.isNameUsed(null);
		} catch (AssertionError e) {
		}
	}

	public void testPlaceCreation() {
		String name, article, description;
		name = "Hall";
		article = "the";
		description = "You are standing in a large hall";
		Place l1 = f_world.createPlace(name, article, description);
		assertEquals(f_world, l1.getWorld());
		assertEquals(l1, f_world.getPlace("HALL"));
		assertEquals(l1, f_world.getPlace("hAll"));
		assertEquals(l1, f_world.getPlaceByName("hall"));
		assertNull(f_world.getPlaceByName("exists"));

		name = "Room";
		article = "the";
		description = "You are standing in a room";
		Place l2 = f_world.createPlace(name, article, description);
		assertEquals(f_world, l2.getWorld());
		assertEquals(l2, f_world.getPlace("ROOM"));
		assertEquals(l2, f_world.getPlace("room"));
		assertEquals(l2, f_world.getPlaceByName("room"));

		assertTrue(f_world.isNameUsed("ROOM"));
		assertTrue(f_world.isNameUsed("HALL"));
		assertFalse(f_world.isNameUsed("UNKNOWN"));

		Set<Place> places = f_world.getPlaces();
		assertEquals(3, places.size());
		assertTrue(places.contains(l1));
		assertTrue(places.contains(l2));
		places.remove(l1);
		assertFalse(places.equals(f_world.getPlaces()));

		try {
			f_world.createPlace("room", article, description);
			fail();
		} catch (IllegalStateException e) {
			// ignore, the creation of a duplicate location should fail
		}
	}

	public void testGameOver() {
		assertFalse(f_world.isGameOver());
		f_world.setGameOver();
		assertTrue(f_world.isGameOver());
	}

	public void testMessage() {
		String m1 = "Hi there";
		String m2 = "How are you?";
		assertEquals("", f_world.getMessage());
		f_world.setMessage(m1);
		assertEquals(m1 + World.LINESEP, f_world.getMessage());
		f_world.addToMessage();
		assertEquals(m1 + World.LINESEP + World.LINESEP, f_world.getMessage());
		f_world.addToMessage(m2);
		assertEquals(m1 + World.LINESEP + World.LINESEP + m2 + World.LINESEP,
				f_world.getMessage());
		f_world.turnOver();
		assertEquals("", f_world.getMessage());
	}

	public void testObservers() {
		IWorldObserver observer = new IWorldObserver() {
			public void update(World world) {
				f_observed = true;
			}
		};

		assertTrue(f_world.getObservers().isEmpty());
		f_observed = false;
		f_world.turnOver();
		assertFalse(f_observed);

		f_world.addObserver(observer);
		assertTrue(f_world.getObservers().contains(observer));
		f_observed = false;
		f_world.turnOver();
		assertTrue(f_observed);

		f_world.removeObserver(observer);
		assertFalse(f_world.getObservers().contains(observer));
		f_observed = false;
		f_world.turnOver();
		assertFalse(f_observed);
	}
}