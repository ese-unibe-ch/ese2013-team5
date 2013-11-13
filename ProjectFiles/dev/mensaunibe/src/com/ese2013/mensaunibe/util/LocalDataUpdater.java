package com.ese2013.mensaunibe.util;

import android.os.AsyncTask;

import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.database.MensaDatabase;

public class LocalDataUpdater extends AsyncTask<Void, Void, Void> {

	private Model model;

	public LocalDataUpdater(Model model) {
		this.model = model;
	}

	/**
	 * Saves the model to the local database
	 */
	@Override
	protected Void doInBackground(Void... arg0) {
		MensaDatabase database = new MensaDatabase();
		database.open();
		database.storeMensas(model.getMensas());
		database.storeFavorites(model.getMensas());
		database.close();
		return null;
	}

}
