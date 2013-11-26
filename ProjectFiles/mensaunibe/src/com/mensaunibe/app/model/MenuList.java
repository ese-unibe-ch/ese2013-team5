package com.mensaunibe.app.model;

import java.io.Serializable;
import java.util.ArrayList;
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
//    	Log.i(TAG, "getAllMenus(), mMenus = " + mMenus);
    	if (mMenus == null) {
    		Log.e(TAG, "mMenus is null");
    		return new ArrayList<Menu>();
    	} else {
    		return mMenus;
    	}
    }
    
	public List<Menu> getMenus(String day) {
		Log.i(TAG, "getMenus()");
		List<Menu> menus = new ArrayList<Menu>();
		for (Menu menu : mMenus) {
			if (menu.getDay() == day) {
				menus.add(menu);
			}
		}
		return menus;
	}
}
