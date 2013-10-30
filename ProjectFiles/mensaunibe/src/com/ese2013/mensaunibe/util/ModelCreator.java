package com.ese2013.mensaunibe.util;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.ese2013.mensaunibe.model.Mensa;

/**
 * AsyncTask to create the model asynchronously
 * 
 * @author ese2013-team5
 * 
 */
public class ModelCreator extends AsyncTask<Void, Void, ArrayList<Mensa>> {

	private AbstractMensaFactory factory;
	private boolean wasSuccessful = false;
	private ArrayList<Mensa> mensas;
	private LoadException exception;

	public ModelCreator(boolean redownloadNeeded) {
		if (redownloadNeeded) {
			factory = new MensaFromWebFactory();
		} else {
			factory = new MensaFromDatabaseFactory();
		}
	}

	@Override
	protected ArrayList<Mensa> doInBackground(Void... arg0) {
		try {
			mensas = factory.createMensaList();
			wasSuccessful = true;
		} catch (LoadException e) {
			exception = e;
			e.printStackTrace();
		}
		return mensas;
	}

	/**
	 * Checks whether the creation was successful or not
	 * 
	 * @return true of the creation was successful else false
	 */
	public boolean wasSuccessful() {
		return wasSuccessful;
	}

	/**
	 * Returns the exception that was thrown during execution of the task
	 * 
	 * @return Exception that was thrown when executing. null if no exception
	 *         was raised
	 */
	public LoadException getException() {
		return exception;
	}

	// /**
	// * Returns the created mensa list. Should only be called before the task
	// was
	// * executed!
	// *
	// * @return The list of the created mensas
	// * @throws LoadException
	// * when the creation of the mensas failed at some point
	// */
	// public ArrayList<Mensa> getMensaList() throws LoadException {
	// if (wasSuccessful) {
	// return mensas;
	// } else {
	// throw exception;
	// }
	// }

	// /**
	// * Informs the model that creation is finished
	// */
	// @Override
	// protected void onPostExecute(Void result) {
	// Model.getInstance().onCreatorFinished(this);
	// super.onPostExecute(result);
	// }

}
