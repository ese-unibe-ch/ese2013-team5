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
}
