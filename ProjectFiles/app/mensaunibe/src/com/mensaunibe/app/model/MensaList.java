package com.mensaunibe.app.model;

import java.io.Serializable;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
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
	
	public Mensa getMensaByLocation(LatLng location) {
		for (Mensa mensa : mensas) {
			if (cutTo5Digits(mensa.getLocation().latitude) == cutTo5Digits(location.latitude) && 
					cutTo5Digits(mensa.getLocation().longitude) == cutTo5Digits(location.longitude)) {
				return mensa;
			}
		}
		return null;
	}
	
	/**
	 * Cut's of overly "precise" location values to 5 digits to make them comparable again
	 * @param d double to cut
	 * @return the rounded value
	 */
	public static double cutTo5Digits(double d) {
	    return Math.floor(d * 1e5) / 1e5;
	}
 
}
