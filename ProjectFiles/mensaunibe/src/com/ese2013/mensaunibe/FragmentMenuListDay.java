/***
  Copyright (c) 2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Android Development_
    http://commonsware.com/Android
 */

package com.ese2013.mensaunibe;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ese2013.mensaunibe.gui_util.AdapterCustomMenulist;
import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Menu;

public class FragmentMenuListDay extends Fragment {
	
	private AdapterCustomMenulist adapter;
	private Context context;
	private ActivityMain main;
	
    private static final String KEY_POSITION = "position";
	private String day;
	private Mensa mensa;

    public static FragmentMenuListDay newInstance(int position, Mensa mensa) {
    	Bundle args = new Bundle();
    	FragmentMenuListDay fragment = null;

        args.putInt(KEY_POSITION, position);
        
	    switch (position) {
		    case 0:
		    	fragment = new FragmentMenuListDay();
		    	fragment.setArguments(args);
		    	fragment.setMensa(mensa);
		    	fragment.setDay("Monday");
		        return fragment;
		    case 1:
		    	fragment = new FragmentMenuListDay();
		    	fragment.setArguments(args);
		    	fragment.setMensa(mensa);
		    	fragment.setDay("Tuesday");
		        return fragment;
		    case 2:
		    	fragment = new FragmentMenuListDay();
		    	fragment.setArguments(args);
		    	fragment.setMensa(mensa);
		    	fragment.setDay("Wednesday");
		        return fragment;
		    case 3:
		    	fragment = new FragmentMenuListDay();
		    	fragment.setArguments(args);
		    	fragment.setMensa(mensa);
		    	fragment.setDay("Thursday");
		        return fragment;
		    case 4:
		    	fragment = new FragmentMenuListDay();
		    	fragment.setArguments(args);
		    	fragment.setMensa(mensa);
		    	fragment.setDay("Friday");
		        return fragment;
		    default:
		        return null;
	    }
    }

	public void setDay(String day) {
		this.day = day;
	}
	
	public void setMensa(Mensa mensa) {
		this.mensa = mensa;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  
        View rootView = inflater.inflate(R.layout.fragment_menulist_pager, container, false);
        this.context = inflater.getContext();
        this.main = (ActivityMain) this.getActivity();
    
        final ListView listview = (ListView) rootView.findViewById(R.id.menulist);
        ArrayList<Menu> menus = new ArrayList<Menu>();
        menus.addAll(mensa.getMenus(day));
    
        adapter = new AdapterCustomMenulist(context, menus, R.layout.list_menulist_item);
    
        listview.setAdapter(adapter);

        return(rootView);
    }
}