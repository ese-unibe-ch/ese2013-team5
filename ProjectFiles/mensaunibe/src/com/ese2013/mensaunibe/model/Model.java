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

	private Model() {
		initialize(isRedownloadNeeded());
		instance = this;
	}

	public ArrayList<Mensa> getMensas() {
		return mensas;
	}

	public Mensa getMensaById(int id) {
		// TODO when saving the mensas in a map rather than a list this should
		// be much cleaner here
		for (int i = 0; i < mensas.size(); i++) {
			if (mensas.get(i).getId() == id) {
				return mensas.get(i);
			}
		}
		return null;
	}

	/**
	 * Returns all the favorite mensas
	 * 
	 * @return List with all the favorite mensas. The List id empty when no
	 *         mensas are favorite
	 */
	public List<Mensa> getFavoriteMensas() {
		List<Mensa> result = new ArrayList<Mensa>();
		for (Mensa m : mensas) {
			if (m.isFavorite())
				result.add(m);
		}
		return result;
	}

	/**
	 * Saves the current state of the model to the database asynchronously
	 */
	public void updateLocalData() {
		LocalDataUpdater updater = new LocalDataUpdater(this);
		updater.execute();
	}

	/**
	 * Get instance of singleton
	 */
	public static Model getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new Model();
			return instance;
		}
	}

	private void initialize(boolean redownloadNeeded) {
		ModelCreator creator = new ModelCreator(redownloadNeeded);
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
		if (!creator.wasSuccessful() && redownloadNeeded) {
			initialize(false);
		} else if (!creator.wasSuccessful()) {
			// TODO do something when model creation failed -> maybe toast then
			// restart?
		}
	}

	private boolean isRedownloadNeeded() {
		// TODO we should check either the api timestamp or a local one in the
		// shared prefs to determine whether the app must redownoload data or
		// not
		return true;
	}

}
