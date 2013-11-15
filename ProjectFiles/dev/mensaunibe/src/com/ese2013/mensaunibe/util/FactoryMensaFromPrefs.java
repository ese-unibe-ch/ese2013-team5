package com.ese2013.mensaunibe.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;

import com.ese2013.mensaunibe.ActivityMain;
import com.ese2013.mensaunibe.model.ModelMensa;
import com.ese2013.mensaunibe.model.ModelWeeklyPlan;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FactoryMensaFromPrefs extends FactoryAbstractMensa {
	
	private ArrayList<ModelMensa> mensas;
	private JsonObject mensasJSON;

	@Override
	public ArrayList<ModelMensa> createMensaList() throws ExceptionLoadPrefs {
		loadAll();
		return mensas;
	}
	
	private void loadAll() throws ExceptionLoadPrefs {
		try {
			mensas = createMensas();
			initializeMenuPlans(mensas);
		} catch (Exception e) {
			throw new ExceptionLoadPrefs(e);
		}
	}

	private ArrayList<ModelMensa> createMensas() throws IOException {
//		MensaDatabase database = new MensaDatabase();
//		database.open();
		ArrayList<ModelMensa> result = new ArrayList<ModelMensa>();
		SharedPreferences settings = ActivityMain.settings;
		
		JsonObject mensasJSON = new JsonParser().parse(settings.getString("mensadata", null)).getAsJsonObject();
		this.mensasJSON = mensasJSON;
		JsonArray array = mensasJSON.getAsJsonArray("mensas");
		for (int i = 0; i < array.size(); i++) {
			BuilderMensa mensaBuilder = new BuilderMensa(array.get(i).getAsJsonObject());
			result.add(mensaBuilder.build());
		}
//		database.close();
		return result;
	}

	private void initializeMenuPlans(List<ModelMensa> mensas) throws IOException {
		for (int i = 0; i < mensas.size(); i++) {
			JsonArray menus = mensasJSON.getAsJsonArray("mensas").get(i).getAsJsonObject().getAsJsonArray("menus");
			mensas.get(i).setWeeklyPlan(new ModelWeeklyPlan(menus, mensas.get(i)));
		}
	}

}
