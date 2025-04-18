package edu.southalabama.csc527.smallworld;

public final class TestConstants {

	/**
	 * The default tempory directory on the computer ended by the correct path
	 * separator for the operating system.
	 */
	public static final String TMP_PATH = System.getProperties().getProperty(
			"java.io.tmpdir", "C:\\")
			+ System.getProperties().getProperty("file.separator");

	/**
	 * A illegal or invalid filename. The value of this constant may not (fail
	 * to) work on all configurations, so change it to one that does (fail) on
	 * your computer.
	 */
	public static final String ILLEGALFILE = "con"; // reserved on Windows

	/**
	 * A filename that is legal, but for which no file exists. The value of this
	 * constant may not (fail to) work on all configurations, so change it to
	 * one that does (fail) on your computer.
	 */
	public static final String MISSINGFILE = TMP_PATH + "nosuch.xml";

	/**
	 * A filename to save and load game saves to and from. The value of this
	 * constant may not work on all configurations, so change it to one that
	 * works on your computer.
	 */
	public static final String SAVEFILE = TMP_PATH + "smallworld_test.xml";

	/**
	 * A URL pointing to a small test world with known data. The path is
	 * relative to the root directory, which is on the classpath.
	 */
	public static final String TESTFILE = "/edu/southalabama/csc527/smallworld/TestWorld.xml";

	/**
	 * A URL pointing to a small test world with known data that contains one or
	 * more errors. The path is relative to the root directory, which is on the
	 * classpath.
	 */
	public static final String TESTFILEWITHERRORS = "/edu/southalabama/csc527/smallworld/TestWorldError.xml";
	
	// added riddle world
	public static final String TEST_RIDDLE_FILE = "/edu/southalabama/csc527/smallworld/TestRiddleWorld.xml";

}
