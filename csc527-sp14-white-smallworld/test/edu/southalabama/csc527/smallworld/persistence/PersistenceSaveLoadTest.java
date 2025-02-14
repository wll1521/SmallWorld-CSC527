package edu.southalabama.csc527.smallworld.persistence;

import java.io.File;

import junit.framework.TestCase;
import edu.southalabama.csc527.smallworld.TestConstants;
import edu.southalabama.csc527.smallworld.model.World;
import edu.southalabama.csc527.smallworld.persistence.WorldPersistence;

public class PersistenceSaveLoadTest extends TestCase {

	public void testLoadIllegalFile() {
		try {
			WorldPersistence.loadWorld(TestConstants.ILLEGALFILE);
			fail();
		} catch (IllegalStateException e) {
		}
	}

	public void testLoadMissingFile() {
		try {
			WorldPersistence.loadWorld(TestConstants.MISSINGFILE);
			fail();
		} catch (IllegalStateException e) {
		}
	}

	public void testLoadFileWithErrors() {
		try {
			WorldPersistence.loadWorld(TestConstants.TESTFILEWITHERRORS);
			fail();
		} catch (IllegalStateException e) {
		}
	}

	public void testSaveLoad() {
		World w = new World();
		try {
			WorldPersistence.saveWorld(w, new File(TestConstants.SAVEFILE));
		} catch (Exception e) {
			fail();
		}
		World w1 = WorldPersistence.loadWorld(new File(TestConstants.SAVEFILE));
		if (w1 == null)
			fail();
	}
}
