package com.ese2013.mensaunibe.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.mensaunibe.app.model.Menu;
import com.mensaunibe.app.model.MenuList;

public class MenuListTest extends TestCase {

	private Menu menu1, menu2;
	private List<Menu> menus;
	private MenuList menuList;

	public MenuListTest() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		menu1 = new Menu(0, 0, "Titel", "Title", "Typ", "Beschreibung",
				"Description", "Preis", "Price", "Datum", "Date", "Day", 0,
				Double.valueOf(0), 0);
		menu2 = new Menu(1, 1, "Titel2", "Title2", "Typ2", "Beschreibung2",
				"Description2", "Preis2", "Price2", "Datum2", "Date2", "Day2",
				1, Double.valueOf(1), 1);
		menus = new ArrayList<Menu>();
		menus.add(menu1);
		menus.add(menu2);
		menuList = new MenuList(menus);
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPrecondiditons() {
		assertNotNull(menu1);
		assertNotNull(menu2);
		assertNotNull(menuList);
		assertNotNull(menus);
	}

	public void testMenuListSHouldEqualItself() {
		assertEquals(menuList, menuList);
	}

	public void testEqualMenuListsShouldBeEqual() {
		MenuList other = new MenuList(menus);
		assertEquals(menuList, other);
	}

	public void testMenuListShouldReturnAllMenus() {
		assertEquals(menus, menuList.getAllMenus());
	}

	public void testGetMenuByCorrectDay() {
		List<Menu> other = new ArrayList<Menu>();
		other.add(menu1);
		assertEquals(other, menuList.getMenus("Day"));
		other.clear();
		other.add(menu2);
		assertEquals(other, menuList.getMenus("Day2"));
	}

	public void testGetMenusByIncorrectDay() {
		List<Menu> other = new ArrayList<Menu>();
		assertEquals(other, menuList.getMenus("not a day"));
	}

	public void testSortingOfTheList() {
		menuList.sortList();
		assertEquals(menu2, menuList.getAllMenus().get(0));
		Menu menu3 = new Menu(1, 1, "Titel2", "Title2", "Typ2", "Beschreibung2",
				"Description2", "Preis2", "Price2", "Datum2", "Date2", "Day2",
				1, Double.valueOf(8), 1);
		menus.add(menu3);
		MenuList newList = new MenuList(menus);
		newList.sortList();
		assertEquals(menu3, newList.getAllMenus().get(0));
		
	}

}
