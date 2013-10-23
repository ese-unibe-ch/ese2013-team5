package com.ese2013.mensaunibe.util;

import java.util.HashSet;
import java.util.Set;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.ese2013.mensaunibe.Home;
import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;

public class LocalDataUpdater extends AsyncTask<Void, Void, Void> {

	private Model model;

	public LocalDataUpdater(Model model) {
		this.model = model;
	}

	/**
	 * Saves all the IDs of the favorite mensas to the shared preferences
	 */
	@Override
	protected Void doInBackground(Void... arg0) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(Home.getContextOfApp());
		SharedPreferences.Editor editor = prefs.edit();
		Set<String> favoriteIds = new HashSet<String>();
		for (Mensa mensa : model.getMensas()) {
			if (mensa.isFavorite())
				favoriteIds.add(Integer.toString(mensa.getId()));
		}
		editor.putStringSet(LocalDataLoader.FAVORITE_MENSAS_KEY, favoriteIds);
		editor.apply();
		return null;
	}

}
