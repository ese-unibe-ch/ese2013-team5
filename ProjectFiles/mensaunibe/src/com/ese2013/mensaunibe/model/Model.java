package com.ese2013.mensaunibe.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ese2013.mensaunibe.util.LocalDataLoader;
import com.ese2013.mensaunibe.util.LocalDataUpdater;
import com.ese2013.mensaunibe.util.BuilderMensa;
import com.ese2013.mensaunibe.util.WebService;

/**
 * Provides the model for the mensa unibe app.
 * 
 * @author ese2013-team5
 * 
 */
public class Model {

	private WebService webService;
	private JSONObject allMensas;
	private ArrayList<Mensa> mensas = new ArrayList<Mensa>();
	private static Model instance;

	public Model() {
		webService = new WebService();
		allMensas = webService.requestAllMensas();
		try {
			createMensalist();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			initializeMenuplans();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		instance = this;
	}

	public ArrayList<Mensa> getMensas() {
		return mensas;
	}

	private void initializeMenuplans() throws JSONException {
		for (int i = 0; i < mensas.size(); i++) {
			JSONObject menus = webService.requestMenusForMensa(mensas.get(i)
					.getId());
			JSONArray array = menus.getJSONArray("menus");
			mensas.get(i).setWeeklyPlan(new WeeklyPlan(array, mensas.get(i)));
		}
	}

	private void createMensalist() throws JSONException {
		LocalDataLoader localDataLoader = new LocalDataLoader();
		localDataLoader.execute();
		try {
			List<Mensa> m = localDataLoader.get();
			if (m.size() > 0) {
				mensas = localDataLoader.get();
				Log.i("Mensas from datbase", "Mensas from database");
			} else
				getMensasFromJSON();
		} catch (Exception e) {
			getMensasFromJSON();
			this.updateLocalData();
		}
	}

	private void getMensasFromJSON() throws JSONException {
		Log.i("Mensas from JSON", "Mensas from JSON");
		JSONArray array = allMensas.getJSONArray("mensas");
		for (int i = 0; i < array.length(); i++) {
			BuilderMensa mensaBuilder = new BuilderMensa(
					array.getJSONObject(i), false/* isInFavIds(i) */);
			mensas.add(mensaBuilder.build());
		}
	}

	public Mensa getMensaById(int id) {
		for (int i = 0; i < mensas.size(); i++) {
			if (mensas.get(i).getId() == id) {
				return mensas.get(i);
			}
		}
		return null;
	}

	public List<Mensa> getFavoriteMensas() {
		List<Mensa> result = new ArrayList<Mensa>();
		for (Mensa m : mensas) {
			if (m.isFavorite())
				result.add(m);
		}
		return result;
	}

	public void updateLocalData() {
		LocalDataUpdater updater = new LocalDataUpdater(this);
		updater.execute();
	}

	public static Model getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new Model();
			return instance;
		}
	}

}
