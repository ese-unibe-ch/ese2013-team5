package com.ese2013.mensaunibe.test;

import java.util.ArrayList;

import android.test.AndroidTestCase;

import com.google.android.gms.internal.db;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.model.Menu;
import com.mensaunibe.app.model.MenuList;
import com.mensaunibe.util.database.DatabaseManager;

public class DatabaseManagerTest extends AndroidTestCase {

	private Mensa mensa1, mensa2;
	private MensaList mensaList;
	private Menu menu1, menu2;
	private MenuList menuList1, menuList2;
	private DatabaseManager dbManager;

	public DatabaseManagerTest() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		initMenus();
		initMenuLists();
		initMensas();
		initMensaList();
		dbManager = new DatabaseManager(getContext());
		super.setUp();
	}

	private void initMenus() {
		menu1 = new Menu(0, 0, "Titel 1", "Title 1", "Typ 1", "Beschreibung 1",
				"Description 1", "Preis 1", "Price 1", "Datum 1", "Date 1",
				"Day 1", 1, Double.valueOf(0), 0);
		menu2 = new Menu(1, 1, "Titel 2", "Title 2", "Typ 2", "Beschreibung 2",
				"Description 2", "Preis 2", "Price 2", "Datum 2", "Date 2",
				"Day 2", 1, Double.valueOf(0), 0);
	}

	private void initMenuLists() {
		ArrayList<Menu> list = new ArrayList<Menu>();
		list.add(menu1);
		menuList1 = new MenuList(list);
		list.clear();
		list.add(menu2);
		menuList2 = new MenuList(list);
	}

	private void initMensas() {
		mensa1 = new Mensa(0, "Ein Name 1", "A Name 1", "Beschriebung 1",
				"Description 1", "Address 1", "City 1", 0, 0, menuList1);
		mensa2 = new Mensa(1, "Ein Name 2", "A Name 2", "Beschriebung 2",
				"Description 2", "Address 2", "City 2", 1, 1, menuList2);
	}

	private void initMensaList() {
		ArrayList<Mensa> list = new ArrayList<Mensa>();
		list.add(mensa1);
		list.add(mensa2);
		mensaList = new MensaList(list);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPrecodnditions() {
		assertNotNull(mensa1);
		assertNotNull(mensa2);
		assertNotNull(mensaList);
		assertNotNull(menu1);
		assertNotNull(menu2);
		assertNotNull(menuList1);
		assertNotNull(menuList2);
		assertNotNull(dbManager);
	}

	public void testAddingAndRemovingMensaToFavorites() {
		mensa1.setFavorite(true);
		dbManager.saveFavorite(mensa1);
		assertTrue(dbManager.isFavorite(mensa1));
		mensa1.setFavorite(false);
		dbManager.removeFavorite(mensa1);
		assertFalse(dbManager.isFavorite(mensa1));
	}

}
