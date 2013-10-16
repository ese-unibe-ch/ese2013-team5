package com.ese2013.mensaunibe;

/**
 * Classes that should be displayed as Lists must implement this interface
 * 
 * @author ese2013-team5
 * 
 */
public interface ListDisplayable {

	/**
	 * Returns the String that should be displayed as the title of the list item
	 */
	public String getTitle();

	/**
	 * Returns the String that should be displayed as the content of the list
	 * item
	 */
	public String getContent();

}
