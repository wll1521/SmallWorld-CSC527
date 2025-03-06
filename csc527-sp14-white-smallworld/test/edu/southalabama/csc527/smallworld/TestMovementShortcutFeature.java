package edu.southalabama.csc527.smallworld;

import junit.framework.TestCase;
import edu.southalabama.csc527.smallworld.controller.WorldController;
import edu.southalabama.csc527.smallworld.model.Direction;
import edu.southalabama.csc527.smallworld.model.Place;
import edu.southalabama.csc527.smallworld.model.World;
import edu.southalabama.csc527.smallworld.textui.parser.IParserObserver;
import edu.southalabama.csc527.smallworld.textui.parser.UserCommandParser;

public class TestMovementShortcutFeature extends TestCase implements
		IParserObserver {

	public void testDirection() {
		// DONE: add tests here for full direction names, e.g., "north" or
		// "NORTH" or "NoRtH", etc.
		
		// Set player location
		f_w.getPlayer().setLocation(f_w.getPlace("C"));
		
		// Feed parser command
		f_parser.parse("north");
		
		// Verifies command worked
		assertEquals("N", f_w.getPlayer().getLocation().getName());
		
		//reset player location
		f_w.getPlayer().setLocation(f_w.getPlace("C"));
		
		// Feed parser command
		f_parser.parse("NORTH");
		
		// Verifies command worked
		assertEquals("N", f_w.getPlayer().getLocation().getName());
		
		// Repeat
		f_w.getPlayer().setLocation(f_w.getPlace("C"));
		
		f_parser.parse("NoRtH");
		
		assertEquals("N", f_w.getPlayer().getLocation().getName());
	}

	public void testAbbreviation() {
		// DONE: add tests here for full abbreviation names, e.g., "n" or "N".
		// Set player location
		f_w.getPlayer().setLocation(f_w.getPlace("C"));
		
		// Feed parser abbreviated command
		f_parser.parse("n");
		
		// Verify command worked
		assertEquals("N", f_w.getPlayer().getLocation().getName());
		
		// Repeat
		f_w.getPlayer().setLocation(f_w.getPlace("C"));
		
		f_parser.parse("N");
		
		assertEquals("N", f_w.getPlayer().getLocation().getName());
	}

	World f_w;

	WorldController f_wc;

	UserCommandParser f_parser;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		f_w = new World();
		f_wc = new WorldController();
		f_wc.setWorld(f_w);
		f_parser = new UserCommandParser(f_wc, this);

		// create a simple world
		Place c = f_w.createPlace("C", "the", "C");
		Place n = f_w.createPlace("N", "the", "N");
		Place s = f_w.createPlace("S", "the", "S");
		Place e = f_w.createPlace("E", "the", "E");
		Place w = f_w.createPlace("W", "the", "W");

		c.setTravelDestination(Direction.NORTH, n);
		n.setTravelDestination(Direction.SOUTH, c);

		c.setTravelDestination(Direction.SOUTH, s);
		s.setTravelDestination(Direction.NORTH, c);

		c.setTravelDestination(Direction.EAST, e);
		e.setTravelDestination(Direction.WEST, c);

		c.setTravelDestination(Direction.WEST, w);
		w.setTravelDestination(Direction.EAST, c);

		f_w.getPlayer().setLocation(c);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		f_parser = null;
		f_wc = null;
		f_w = null;
	}

	public void look(World world) {
		// do nothing

	}

	public void display(String msg) {
		// do nothing
	}
}
