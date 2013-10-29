package com.ese2013.mensaunibe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ese2013.mensaunibe.util.LocalDataUpdater;
import com.ese2013.mensaunibe.util.ModelCreator;

/**
 * Provides the model for the mensa unibe app.
 * 
 * @author ese2013-team5
 * 
 */
public class Model {

	private ArrayList<Mensa> mensas = new ArrayList<Mensa>();
	private static Model instance;

	public Model() {
		initialize();
		instance = this;
	}

	public ArrayList<Mensa> getMensas() {
		return mensas;
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
	
	private void initialize() {
		ModelCreator creator = new ModelCreator(false);
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
	}

}
