package com.ese2013.mensaunibe.util;

import java.util.ArrayList;

import com.ese2013.mensaunibe.model.ModelMensa;

/**
 * Abstract factory to create the mensas
 * 
 * @author ese2013-team5
 *
 */
public abstract class FactoryAbstractMensa {

	/**
	 * Loads and instantiates the mensa list 
	 */
	public abstract ArrayList<ModelMensa> createMensaList() throws ExceptionLoadPrefs, ExceptionLoadDatabase, ExceptionLoadWeb;

}
