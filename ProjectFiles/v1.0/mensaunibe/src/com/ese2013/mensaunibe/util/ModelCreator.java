package com.ese2013.mensaunibe.util;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.widget.Toast;

import com.ese2013.mensaunibe.ActivityMain;
import com.ese2013.mensaunibe.model.ModelMensa;

/**
 * AsyncTask to create the model asynchronously
 * 
 * @author ese2013-team5
 * 
 */
public class ModelCreator extends AsyncTask<Void, Void, ArrayList<ModelMensa>> {

	private FactoryAbstractMensa factory;
	private boolean wasSuccessful = false;
	private ArrayList<ModelMensa> mensas;
	private ExceptionLoad exception;

	public ModelCreator(boolean redownloadNeeded) {
		if (redownloadNeeded) {
			Toast.makeText(ActivityMain.getContext(), "fetching data from API", Toast.LENGTH_LONG).show();
			factory = new FactoryMensaFromWeb();
		} else {
			Toast.makeText(ActivityMain.getContext(), "fetching data from Memory", Toast.LENGTH_LONG).show();
			factory = new FactoryMensaFromPrefs();
//			factory = new FactoryMensaFromDatabase();
		}
	}

	@Override
	protected ArrayList<ModelMensa> doInBackground(Void... arg0) {
		try {
			mensas = factory.createMensaList();
			wasSuccessful = true;
		} catch (ExceptionLoad e) {
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
	public ExceptionLoad getException() {
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
