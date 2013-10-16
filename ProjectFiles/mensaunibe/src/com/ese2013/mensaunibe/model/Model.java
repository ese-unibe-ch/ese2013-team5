package com.ese2013.mensaunibe.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Provides the model for the mensa unibe app.
 * 
 * @author ese2013-team5
 * 
 */
// TODO maybe discuss better name and responsibilities for this class
public class Model {

	MensaWebService webService;
	JSONObject allMensas;
	ArrayList<Mensa> Mensas = new ArrayList<Mensa>();

	public Model() {
		webService = new MensaWebService();
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
	}

	// TODO Adapt to work with the new WeeklyPlan, DailyPlan and menu builder:
	// for each mensa id request the menu plan pass it to the WeeklyPlan
	// constructor, then assign the new plan to the mensa
	private void initializeMenuplans() throws JSONException {
		for (int i = 0; i < Mensas.size(); i++) {
			JSONObject menus = webService.requestMenusForMensa(Mensas.get(i)
					.getId());
			JSONArray array = menus.getJSONObject("result")
					.getJSONObject("content").getJSONArray("menus");
			Mensas.get(i).setWeeklyPlan(new WeeklyPlan(array));
		}
	}

	// TODO Adapt to work with the new mensa builder: createMensalist() should
	// get the JSONObject returned from the request, then go through each
	// JSONObject in the JSONArray and pass it to the builder
	// (Mensa.MensaBuilder(jsonForMensa).build(); ) and then add the returned
	// mensas to the list
	private void createMensalist() throws JSONException {
		JSONArray array = allMensas.getJSONObject("result").getJSONArray(
				"content");
		for (int i = 0; i < array.length(); i++) {
			Mensa.MensaBuilder mensaBuilder = new Mensa.MensaBuilder(
					array.getJSONObject(i));
			Mensas.add(mensaBuilder.build());
		}
	}

	public ArrayList<Mensa> getMensas() {
		return Mensas;
	}

}
