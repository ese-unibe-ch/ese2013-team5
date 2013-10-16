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

	public ArrayList<Mensa> getMensas() {
		return Mensas;
	}

	private void initializeMenuplans() throws JSONException {
		for (int i = 0; i < Mensas.size(); i++) {
			JSONObject menus = webService.requestMenusForMensa(Mensas.get(i)
					.getId());
			JSONArray array = menus.getJSONObject("result")
					.getJSONObject("content").getJSONArray("menus");
			Mensas.get(i).setWeeklyPlan(new WeeklyPlan(array));
		}
	}

	private void createMensalist() throws JSONException {
		JSONArray array = allMensas.getJSONObject("result").getJSONArray(
				"content");
		for (int i = 0; i < array.length(); i++) {
			MensaBuilder mensaBuilder = new MensaBuilder(
					array.getJSONObject(i));
			Mensas.add(mensaBuilder.build());
		}
	}

}
