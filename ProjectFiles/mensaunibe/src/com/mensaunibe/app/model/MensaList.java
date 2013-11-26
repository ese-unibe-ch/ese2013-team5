package com.mensaunibe.app.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
 
public class MensaList implements Serializable {
	 
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = MensaList.class.getSimpleName();
	private static final long serialVersionUID = 4863765091459252275L;
 
    @SerializedName( "mensas" )
    private final List<Mensa> mensas;
 
    public MensaList(final List<Mensa> mensas) {
        this.mensas = mensas;
    }
 
    public List<Mensa> getMensas() {
        return mensas;
    }
    
	public Mensa getMensaById(int id) {
		for (Mensa mensa : mensas) {
			if (mensa.getId() == id) {
				return mensa;
			}
		}
		return null;
	}
 
}
