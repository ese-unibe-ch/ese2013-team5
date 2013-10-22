package com.ese2013.mensaunibe.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	MensaWebService webService;
	JSONObject allMensas;
	ArrayList<Mensa> mensas = new ArrayList<Mensa>();

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
					array.getJSONObject(i));
			mensas.add(mensaBuilder.build());
		}
	}

	public Mensa getMensaById(int id) {
		for(int i = 0; i < mensas.size(); i++){
			if(mensas.get(i).getId() == id){
				return mensas.get(i);
			}
		}
		return null;
	}

}
