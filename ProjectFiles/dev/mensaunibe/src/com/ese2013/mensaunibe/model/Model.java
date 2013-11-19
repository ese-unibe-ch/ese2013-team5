package com.ese2013.mensaunibe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.SharedPreferences;

import com.ese2013.mensaunibe.ActivityMain;
import com.ese2013.mensaunibe.ActivityMain.AsyncCallback;
import com.ese2013.mensaunibe.util.LocalDataUpdater;
import com.ese2013.mensaunibe.util.ModelCreator;
import com.ese2013.mensaunibe.util.WebServiceAsync;
import com.google.gson.JsonObject;

/**
 * Provides the model for the mensa unibe app.
 * 
 * @author ese2013-team5
 * 
 */
public class Model {

	private ArrayList<ModelMensa> mensas = new ArrayList<ModelMensa>();
	public int lastupdateapi;
	public boolean isRedownloadNeeded;
	private static Model instance;

	private Model() {
		initialize(isRedownloadNeeded());
		instance = this;
	}

	public ArrayList<ModelMensa> getMensas() {
		return mensas;
	}

	public ModelMensa getMensaById(int id) {
		// TODO when saving the mensas in a map rather than a list this should
		// be much cleaner here
		for (int i = 0; i < mensas.size(); i++) {
			if (mensas.get(i).getId() == id) {
				return mensas.get(i);
			}
		}
		return null;
	}

	/**
	 * Returns all the favorite mensas
	 * 
	 * @return List with all the favorite mensas. The List id empty when no
	 *         mensas are favorite
	 */
	public List<ModelMensa> getFavoriteMensas() {
		List<ModelMensa> result = new ArrayList<ModelMensa>();
		for (ModelMensa m : mensas) {
			if (m.isFavorite()) {
				result.add(m);
			}
		}
		return result;
	}

	/**
	 * Saves the current state of the model to the database asynchronously
	 */
	public void updateLocalData() {
		LocalDataUpdater updater = new LocalDataUpdater(this);
		updater.execute();
	}

	/**
	 * Get instance of singleton
	 */
	public static Model getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new Model();
			return instance;
		}
	}

	private void initialize(boolean redownloadNeeded) {
		ModelCreator creator = new ModelCreator(redownloadNeeded);
		creator.execute();
		try {
			mensas = creator.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!creator.wasSuccessful() && redownloadNeeded) {
			initialize(false);
		} else if (!creator.wasSuccessful()) {
			// TODO do something when model creation failed -> maybe toast then
			// restart?
		}
	}

	private boolean isRedownloadNeeded() {
		// get the last update date from the API
		String url = "http://api.031.be/mensaunibe/v1/?type=lastupdate";
		new WebServiceAsync(new AsyncCallback() { 

			@Override
            public void onTaskDone(JsonObject json) {
				lastupdateapi = json.get("lastupdate").getAsInt();
				//Toast.makeText(ActivityMain.getContext(), "last update onTaskDone(): " + lastupdateapi, Toast.LENGTH_LONG).show();
				// now compare this timestamp to the local one in the shared prefs
				SharedPreferences settings = ActivityMain.settings;

				//Toast.makeText(ActivityMain.getContext(), "lastupdate from API: " + lastupdateapi, Toast.LENGTH_LONG).show();
				//Toast.makeText(ActivityMain.getContext(), "lastupdate from Prefs: " + settings.getInt("lastupdate", 0), Toast.LENGTH_LONG).show();
				if ( lastupdateapi != settings.getInt("lastupdate", 0)) {
					isRedownloadNeeded = true;
				} else {
					isRedownloadNeeded = false;
				}
            }
	    }).execute(url); // start the background processing
		
		return isRedownloadNeeded;
	}
}
