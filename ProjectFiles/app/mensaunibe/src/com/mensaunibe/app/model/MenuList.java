package com.mensaunibe.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
 
import android.util.Log;

import com.google.gson.annotations.SerializedName;
 
public class MenuList implements Serializable {
	
	// for logging and debugging purposes
	private static final String TAG = MenuList.class.getSimpleName();
	private static final long serialVersionUID = 7845443203230432605L;
	
	@SerializedName("menus")
    private final List<Menu> mMenus;
 
    public MenuList(final List<Menu> menus) {
        this.mMenus = menus;
    }
 
    public List<Menu> getAllMenus() {
    	if (mMenus == null) {
    		Log.e(TAG, "mMenus is null");
    		return new ArrayList<Menu>();
    	} else {
    		sortList();
    		return mMenus;
    	}
    }
    
	public List<Menu> getMenus(String day) {
		Log.i(TAG, "getMenus()");
		List<Menu> menus = new ArrayList<Menu>();
		sortList();
		for (Menu menu : mMenus) {
			if (menu.getDay() == day) {
				menus.add(menu);
			}
		}
		return menus;
	}
	
	/**
	 * sorts the list that menu with the best ratings are displayed first
	 */
	public void sortList() {
		Collections.sort(mMenus, new Comparator<Menu>(){
		    public int compare(Menu m1, Menu m2) {
		    	int result = m2.getRating().compareTo(m1.getRating());
		        return result;
		    }
		});
	}
}
