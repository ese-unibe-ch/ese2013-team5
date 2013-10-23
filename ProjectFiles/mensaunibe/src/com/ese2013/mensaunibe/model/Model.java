package com.ese2013.mensaunibe.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ese2013.mensaunibe.util.LocalDataLoader;
import com.ese2013.mensaunibe.util.LocalDataUpdater;
import com.ese2013.mensaunibe.util.MensaBuilder;
import com.ese2013.mensaunibe.util.MensaWebService;

/**
 * Provides the model for the mensa unibe app.
 * 
 * @author ese2013-team5
 * 
 */
// TODO maybe discuss better name and responsibilities for this class
public class Model {

	private MensaWebService webService;
	private LocalDataLoader localDataLoader;
	private LocalDataUpdater localDataUpdater;
	private JSONObject allMensas;
	private ArrayList<Mensa> mensas = new ArrayList<Mensa>();
	private Set<Integer> favoriteIds;
	private static Model instance;

	public Model() {
		webService = new MensaWebService();
		localDataLoader = new LocalDataLoader();
		favoriteIds = getFavoriteIds();
		localDataUpdater = new LocalDataUpdater(this);
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

			JSONArray array = menus.getJSONObject("result")
					.getJSONObject("content").getJSONArray("menus");
<<<<<<< HEAD
<<<<<<< HEAD
			Mensas.get(i).setWeeklyPlan(new WeeklyPlan(array));

=======
			Mensas.get(i).setWeeklyPlan(new WeeklyPlan(array, Mensas.get(i)));
>>>>>>> original/master
=======
			mensas.get(i).setWeeklyPlan(new WeeklyPlan(array, mensas.get(i)));
>>>>>>> original/master
		}
	}

	private void createMensalist() throws JSONException {
		JSONArray array = allMensas.getJSONObject("result").getJSONArray(
				"content");
		for (int i = 0; i < array.length(); i++) {
			MensaBuilder mensaBuilder = new MensaBuilder(
					array.getJSONObject(i), isInFavIds(i));
			mensas.add(mensaBuilder.build());
		}
	}
	
	private Set<Integer> getFavoriteIds() {
		localDataLoader.execute();
		Set<Integer> result = new HashSet<Integer>();
		try {
			result = localDataLoader.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private boolean isInFavIds(int id) {
		return favoriteIds.contains(id);
	}

	public Mensa getMensaById(int id) {
		for(int i = 0; i < mensas.size(); i++){
			if(mensas.get(i).getId() == id){
				return mensas.get(i);
			}
		}
		return null;
	}
	
	public void updateLocalData() {
		localDataUpdater.execute();
	}
	
	public static Model getInstance() {
		if (instance != null) {
			return instance;
		}
		else {
			instance = new Model();
			return instance;
		}
	}

}
