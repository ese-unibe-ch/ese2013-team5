package com.ese2013.mensaunibe.util;

import java.util.HashSet;
import java.util.Set;

import com.ese2013.mensaunibe.Home;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class LocalDataLoader extends AsyncTask<Void, Void, Set<Integer>> {

	public static final String FAVORITE_MENSAS_KEY = "favorite mensas";

	public LocalDataLoader() {
	}

	/**
	 * Loads all the IDs of the favorite mensas from the shared preferences
	 */
	@Override
	protected Set<Integer> doInBackground(Void... params) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(Home.getContextOfApp());
		Set<String> favs = prefs.getStringSet(FAVORITE_MENSAS_KEY,
				new HashSet<String>());
		Set<Integer> result = new HashSet<Integer>();
		for (String s : favs) {
			result.add(Integer.parseInt(s));
		}
		return result;
	}

}
