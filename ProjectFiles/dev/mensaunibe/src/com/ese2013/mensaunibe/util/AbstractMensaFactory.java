package com.ese2013.mensaunibe.util;

import java.util.ArrayList;

import com.ese2013.mensaunibe.model.Mensa;

/**
 * Abstract factory to create the mensas
 * 
 * @author ese2013-team5
 *
 */
public abstract class AbstractMensaFactory {

	/**
	 * Loads and instantiates the mensa list 
	 */
	public abstract ArrayList<Mensa> createMensaList() throws SqlLoadException, WebLoadException;

}
