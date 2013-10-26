package com.ese2013.mensaunibe.util;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.util.database.MensaDatabase;

public class LocalDataLoader extends AsyncTask<Void, Void, ArrayList<Mensa>> {

	public static final String FAVORITE_MENSAS_KEY = "favorite mensas";

	public LocalDataLoader() {
	}

	/**
	 * Loads all the mensas from the database
	 */
	@Override
	protected ArrayList<Mensa> doInBackground(Void... params) {
		MensaDatabase database = new MensaDatabase();
		database.open();
		ArrayList<Mensa> result = database.loadAllMensas();
		database.close();
		return result;
	}

//	/**
//	 * Loads all the IDs of the favorite mensas from the shared preferences
//	 */
//	@Override
//	protected Set<Integer> doInBackground(Void... params) {
//		SharedPreferences prefs = PreferenceManager
//				.getDefaultSharedPreferences(ActivityMain.getContextOfApp());
//		Set<String> favs = prefs.getStringSet(FAVORITE_MENSAS_KEY,
//				new HashSet<String>());
//		Set<Integer> result = new HashSet<Integer>();
//		for (String s : favs) {
//			result.add(Integer.parseInt(s));
//		}
//		return result;
//	}

}
