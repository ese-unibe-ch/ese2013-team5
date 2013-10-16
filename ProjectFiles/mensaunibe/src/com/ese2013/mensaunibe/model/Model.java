package com.ese2013.mensaunibe.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
			JSONArray array = menus.getJSONArray("menus");
			for (int j = 0; j < array.length(); j++) {
				String title = array.getJSONObject(j).getString("title");
				String date = array.getJSONObject(j).getString("date");
				String day = array.getJSONObject(j).getString("day");
				String[] menuDetails = (String[]) array.getJSONObject(j).get(
						"menu");
				String price = menuDetails[menuDetails.length - 1];
//				Menu menu = new Menu(title, date, price, menuDetails, Mensas
//						.get(i).getId());
				Menu menu = new Menu.MenuBuilder(menus).build();
				Mensas.get(i).getDailyPlan(day).add(menu);
			}
		}
	}

	// TODO Adapt to work with the new mensa builder: createMensalist() should
	// get the JSONObject returned from the request, then go through each
	// JSONObject in the JSONArray and pass it to the builder
	// (Mensa.MensaBuilder(jsonForMensa).build(); ) and then add the returned
	// mensas to the list
	private void createMensalist() throws JSONException {
		// This line caused an Exception for me (nicolas)
		JSONArray array = allMensas.getJSONArray("content");
		for (int i = 0; i < array.length(); i++) {
			int id = array.getJSONObject(i).getInt("id");
			String name = array.getJSONObject(i).getString("mensa");
			String street = array.getJSONObject(i).getString("street");
			String plz = array.getJSONObject(i).getString("plz");
			float lat = (Float) array.getJSONObject(i).get("lat");
			float lon = (Float) array.getJSONObject(i).get("lon");
			Mensas.add(new Mensa(id, name, street, plz, lat, lon));
		}
	}

}
