package edu.southalabama.csc527.smallworld;

import static edu.southalabama.csc527.smallworld.model.Direction.EAST;

import java.io.File;

import junit.framework.TestCase;
import edu.southalabama.csc527.smallworld.controller.WorldController;
import edu.southalabama.csc527.smallworld.model.IWorldObserver;
import edu.southalabama.csc527.smallworld.model.Place;
import edu.southalabama.csc527.smallworld.model.World;
import edu.southalabama.csc527.smallworld.persistence.WorldPersistence;

public class TestArrivalWinsGameFeature extends TestCase implements
    IWorldObserver {

  public static final String TEST_WORLD = "/edu/southalabama/csc527/smallworld/TestArrivalWinsGameWorld.xml";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    f_readyToWin = false;
    f_didWeWin = false;
  }

  boolean f_readyToWin = false;

  boolean f_didWeWin = false;

  public void update(World world) {
    if (world.isGameOver()) {
      if (f_readyToWin) {
        f_didWeWin = true;
      } else {
        fail("won the game before we arrived in the Hall");
      }
    }
  }

  public void testModelController() {
    World w = new World();
    w.addObserver(this);
    WorldController wc = new WorldController();
    wc.setWorld(w);

    Place building = w.createPlace("Building", "the", "");
    building.setArrivalWinsGame(true);
    Place road = w.createPlace("Road", "a", "");

    road.setTravelDestination(EAST, building);
    w.getPlayer().setLocation(road);

    // play the game without the UI...starting at the Road
    f_readyToWin = true;
    wc.travel(EAST);
    if (!f_didWeWin)
      fail("we didn't win the game as we arrived in the Building");
  }

  public void testLoadedWorld() {
    World w = WorldPersistence.loadWorld(TEST_WORLD);
    w.addObserver(this);
    WorldController wc = new WorldController();
    wc.setWorld(w);

    // play the game without the UI...starting at the Road
    wc.travel(EAST); // to the Building
    f_readyToWin = true;
    wc.travel(EAST); // to the Hall (we should now win).
    if (!f_didWeWin) fail("we didn't win the game as we arrived in the Hall");
  }

  public void testSavedWorld() {
    World w = new World();
    Place building = w.createPlace("Building", "the", "");
    building.setArrivalWinsGame(true);
    Place road = w.createPlace("Road", "a", "");
    road.setTravelDestination(EAST, building);
    w.getPlayer().setLocation(road);
    WorldPersistence.saveWorld(w, new File(TestConstants.SAVEFILE));

    World w1 = WorldPersistence.loadWorld(new File(TestConstants.SAVEFILE));
    assertTrue(w1.getPlace("BUILDING").arrivalWinsGame());
    assertFalse(w1.getPlace("ROAD").arrivalWinsGame());
    assertFalse(w1.getNowherePlace().arrivalWinsGame());
    w1.addObserver(this);
    WorldController wc = new WorldController();
    wc.setWorld(w1);
    // play the game without the UI...starting at the Road
    f_readyToWin = true;
    wc.travel(EAST);
    if (!f_didWeWin)
      fail("we didn't win the game as we arrived in the Building");
  }
}
