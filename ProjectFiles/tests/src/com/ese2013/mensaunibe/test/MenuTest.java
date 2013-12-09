package com.ese2013.mensaunibe.test;

import junit.framework.TestCase;

import com.mensaunibe.app.model.Menu;

public class MenuTest extends TestCase {

	private Menu menu1, menuEqualTo1;

	public MenuTest() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		menu1 = new Menu(0, 0, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day", 0,
				Double.valueOf(0), 0);
		menuEqualTo1 = new Menu(0, 0, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day", 0,
				Double.valueOf(0), 0);
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPreconditions() {
		assertNotNull(menu1);
		assertNotNull(menuEqualTo1);
	}

	public void testMenuEqualsItself() {
		assertEquals(menu1, menu1);
	}

	public void testEqualMenusShouldBeEqual() {
		assertEquals(menu1, menuEqualTo1);
	}

	public void testMenusDifferingInIdShouldBeUnequal() {
		Menu other = new Menu(1, 0, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day", 0,
				Double.valueOf(0), 0);
		assertFalse(menu1.equals(other));
	}
	
	public void testMenusDifferingInMensaIdShouldBeUnequal() {
		Menu other = new Menu(0, 1, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day", 0,
				Double.valueOf(0), 0);
		assertFalse(menu1.equals(other));
	}
	
	public void testMenusDifferingInTitleShouldBeUnequal() {
		Menu other = new Menu(0, 0, "Titel2", "Title2", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day", 0,
				Double.valueOf(0), 0);
		assertFalse(menu1.equals(other));
	}
	
	public void testMenusDifferingInTypeShouldBeUnequal() {
		Menu other = new Menu(0, 0, "Titel", "Title", "Typ2", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day", 0,
				Double.valueOf(0), 0);
		assertFalse(menu1.equals(other));
	}
	
	public void testMenusDifferingInDescriptionShouldBeUnequal() {
		Menu other = new Menu(0, 0, "Titel", "Title", "Typ", "Beschreibung2",
				"Description2", "Preis", "Price", "Datum", "Date", "Day", 0,
				Double.valueOf(0), 0);
		assertFalse(menu1.equals(other));
	}
	
	public void testMenusDifferingInPriceShouldBeUnequal() {
		Menu other = new Menu(0, 0, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis2", "Price2", "Datum", "Date", "Day", 0,
				Double.valueOf(0), 0);
		assertFalse(menu1.equals(other));
	}
	
	public void testMenusDifferingInDateShouldBeUnequal() {
		Menu other = new Menu(0, 0, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum2", "Date2", "Day", 0,
				Double.valueOf(0), 0);
		assertFalse(menu1.equals(other));
	}
	
	public void testMenusDifferingInDayShouldBeUnequal() {
		Menu other = new Menu(0, 0, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day2", 0,
				Double.valueOf(0), 0);
		assertFalse(menu1.equals(other));
	}
	
	public void testMenusDifferingInWeekShouldBeUnequal() {
		Menu other = new Menu(0, 0, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day", 1,
				Double.valueOf(0), 0);
		assertFalse(menu1.equals(other));
	}
	
	public void testMenusDifferingInRatingShouldBeEqual() {
		Menu other = new Menu(0, 0, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day", 0,
				Double.valueOf(2), 0);
		assertTrue(menu1.equals(other));
	}
	
	public void testMenusDifferingInTitleShouldBeEqual() {
		Menu other = new Menu(0, 0, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day", 0,
				Double.valueOf(0), 1);
		assertTrue(menu1.equals(other));
	}

}
