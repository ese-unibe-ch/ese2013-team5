package com.ese2013.mensaunibe.test;

import junit.framework.TestCase;

import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MenuList;

public class MensaTest extends TestCase {

	private Mensa mensa1, mensaEqualTo1;

	public MensaTest() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		mensa1 = new Mensa(0, "Ein Name", "A Name", "Beschriebung",
				"Description", "Address", "City", 0, 0, null);
		mensaEqualTo1 = new Mensa(0, "Ein Name", "A Name", "Beschriebung",
				"Description", "Address", "City", 0, 0, null);
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPreconditions() {
		assertNotNull(mensa1);
		assertNotNull(mensaEqualTo1);
	}

	public void testMensaEqualsItself() {
		assertEquals(mensa1, mensa1);
		assertEquals(mensaEqualTo1, mensaEqualTo1);
	}

	public void testEqualMensasShouldBeEqual() {
		assertTrue(mensa1.equals(mensaEqualTo1));
	}

	public void testMensasDifferingInNamesShouldBeUnequal() {
		// Note that here the translated names will not be tested as the
		// translation happens in the api and is not the responsibility of a
		// mensa. The German fields are used for comparing and it is assumed
		// that the translations are correct
		Mensa otherMensa = new Mensa(0, "Ein Name2", "A Name2", "Beschriebung",
				"Description", "Address", "City", 0, 0, null);
		assertFalse(mensa1.equals(otherMensa));
	}

	public void testMensasDifferingInDescriptionShouldBeUnequal() {
		// For the description the same applies as for the names (see above)
		Mensa otherMensa = new Mensa(0, "Ein Name", "A Name", "Beschriebung2",
				"Description", "Address", "City", 0, 0, null);
		assertFalse(mensa1.equals(otherMensa));
	}

	public void testMensasDifferingInAddressShouldBeUnequal() {
		Mensa otherMensa = new Mensa(0, "Ein Name", "A Name", "Beschriebung",
				"Description", "Address2", "City", 0, 0, null);
		assertFalse(mensa1.equals(otherMensa));
	}

	public void testMensasDifferingInCityShouldBeUnequal() {
		Mensa otherMensa = new Mensa(0, "Ein Name", "A Name", "Beschriebung",
				"Description", "Address", "City2", 0, 0, null);
		assertFalse(mensa1.equals(otherMensa));
	}

	public void testMensasDifferingInLatShouldBeUnequal() {
		Mensa otherMensa = new Mensa(0, "Ein Name", "A Name", "Beschriebung",
				"Description", "Address", "City", 1, 0, null);
		assertFalse(mensa1.equals(otherMensa));
	}

	public void testMensasDifferingInLonShouldBeUnequal() {
		Mensa otherMensa = new Mensa(0, "Ein Name", "A Name", "Beschriebung",
				"Description", "Address", "City", 0, 1, null);
		assertFalse(mensa1.equals(otherMensa));
	}
	
	public void testMensasDifferingInMenuListShouldBeEqual() {
		Mensa otherMensa = new Mensa(0, "Ein Name", "A Name", "Beschriebung",
				"Description", "Address", "City", 0, 0, new MenuList(null));
		assertTrue(mensa1.equals(otherMensa));
	}

}
