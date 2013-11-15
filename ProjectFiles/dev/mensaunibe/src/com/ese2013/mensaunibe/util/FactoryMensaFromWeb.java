package com.ese2013.mensaunibe.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences.Editor;

import com.ese2013.mensaunibe.ActivityMain;
import com.ese2013.mensaunibe.ActivityMain.AsyncCallback;
import com.ese2013.mensaunibe.model.ModelMensa;
import com.ese2013.mensaunibe.model.ModelWeeklyPlan;
import com.ese2013.mensaunibe.util.database.MensaDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class FactoryMensaFromWeb extends FactoryAbstractMensa {
	
	private ArrayList<ModelMensa> mensas;
	private JsonObject mensasJSON;
	protected ArrayList<ModelMensa> mensasArrayList;

	@Override
	public ArrayList<ModelMensa> createMensaList() throws ExceptionLoadWeb {
		loadAll();
		return mensas;
	}
	
	private void loadAll() throws ExceptionLoadWeb {
		try {
			mensas = createMensas();
			initializeMenuPlans(mensas);
		} catch (Exception e) {
			throw new ExceptionLoadWeb(e);
		}
	}

	private ArrayList<ModelMensa> createMensas() throws IOException {
		MensaDatabase database = new MensaDatabase();
		database.open();
//		JsonObject mensasJSON = webService.requestFromURL("http://api.031.be/mensaunibe/v1/?type=mensafull");
		String url = "http://api.031.be/mensaunibe/v1/?type=mensafull";
		new WebServiceAsync(new AsyncCallback() { 

			@Override
            public void onTaskDone(JsonObject json) {
				ArrayList<ModelMensa> result = new ArrayList<ModelMensa>();
				// shouldn't the data be written to the database somewhere here?
				// save JSON string to shared prefs for offline capability
				Editor settingseditor = ActivityMain.settingseditor;
				settingseditor.putString("mensadata", json.toString());
				// save last update timestamp to shared prefs for later checks / offline capability
				settingseditor.putInt("lastupdate", json.get("lastupdatetimestamp").getAsInt());
				settingseditor.commit();
				mensasJSON = json;
				
				JsonArray array = json.get("mensas").getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					BuilderMensa mensaBuilder = new BuilderMensa(array.get(i).getAsJsonObject());
					result.add(mensaBuilder.build());
				}
				mensasArrayList = result;
            }
	    }).execute(url); // start the background processing
		database.close();
		return mensasArrayList;
	}

	private void initializeMenuPlans(List<ModelMensa> mensas) throws IOException {
		for (int i = 0; i < mensas.size(); i++) {
			JsonArray menus = ((JsonElement) mensasJSON.get("mensas")).getAsJsonArray().get(i).getAsJsonObject().get("menus").getAsJsonArray();
			mensas.get(i).setWeeklyPlan(new ModelWeeklyPlan(menus, mensas.get(i)));
		}
	}

}
