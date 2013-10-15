package com.ese2013.mensaunibe.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Model {

	MensaWebService webService;
	JSONObject allMensas;
	ArrayList<Mensa> Mensas = new ArrayList<Mensa>();
	
	public Model(){
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
	
	private void initializeMenuplans() throws JSONException {
		for(int i = 0; i < Mensas.size(); i++){
			JSONObject menus = webService.requestMenusForMensa(Mensas.get(i).getId());
			JSONArray array = menus.getJSONArray("menus");
			for(int j = 0; j < array.length(); j++){
				String title = array.getJSONObject(j).getString("title");
				String date = array.getJSONObject(j).getString("date");
				String day = array.getJSONObject(j).getString("day");
				String[] menuDetails = (String[]) array.getJSONObject(j).get("menu");
				String price = menuDetails[menuDetails.length-1];
				Menu menu = new Menu(title, date, price, menuDetails, Mensas.get(i).getId());
				Mensas.get(i).getDailyPlan(day).add(menu);
			}
		}
	}

	private void createMensalist() throws JSONException{
		JSONArray array = allMensas.getJSONArray("content");
		for(int i = 0 ;i < array.length(); i++){
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
