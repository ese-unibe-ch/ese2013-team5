package com.ese2013.mensaunibe.util;

import java.util.ArrayList;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.util.database.MensaDatabase;

/**
 * Factory that uses the local database to create the mensa list
 * 
 * @author ese2013-team5
 * 
 */
public class MensaFromDatabaseFactory extends AbstractMensaFactory {

	@Override
	public ArrayList<Mensa> createMensaList() throws SqlLoadException {
		MensaDatabase database = new MensaDatabase();
		try {
			database.open();
			ArrayList<Mensa> result = database.loadAllMensas();
			for (Mensa m : result) {
				m.setWeeklyPlan(database.loadPlanForMensa(m));
			}
			return result;
		} catch (Exception e) {
			throw new SqlLoadException(e);
		} finally {
			database.close();
		}
	}

}
