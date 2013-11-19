package com.ese2013.mensaunibe.util;

import java.util.ArrayList;

import com.ese2013.mensaunibe.model.ModelMensa;
import com.ese2013.mensaunibe.util.database.MensaDatabase;

/**
 * Factory that uses the local database to create the mensa list
 * 
 * @author ese2013-team5
 * 
 */
public class FactoryMensaFromDatabase extends FactoryAbstractMensa {

	@Override
	public ArrayList<ModelMensa> createMensaList() throws ExceptionLoadDatabase {
		MensaDatabase database = new MensaDatabase();
		try {
			database.open();
			ArrayList<ModelMensa> result = database.loadAllMensas();
			for (ModelMensa m : result) {
				m.setWeeklyPlan(database.loadPlanForMensa(m));
			}
			return result;
		} catch (Exception e) {
			throw new ExceptionLoadDatabase(e);
		} finally {
			database.close();
		}
	}

}
