package com.ese2013.mensaunibe.util;

import android.os.AsyncTask;

import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.database.MensaDatabase;

public class LocalDataUpdater extends AsyncTask<Void, Void, Void> {

	private Model model;
	private MensaDatabase database;

	public LocalDataUpdater(Model model) {
		this.model = model;
		this.database = new MensaDatabase();
	}

	/**
	 * Saves all the IDs of the favorite mensas to the shared preferences
	 */
	@Override
	protected Void doInBackground(Void... arg0) {
		 database.open();
		 database.storeMensas(model.getMensas());
		 database.close();
		 return null;
	}

}
