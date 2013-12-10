package com.mensaunibe.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
 
public class MensaList implements Serializable {
	 
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = MensaList.class.getSimpleName();
	private static final long serialVersionUID = 4863765091459252275L;
 
    @SerializedName( "mensas" )
    private final List<Mensa> mMensas;
 
    public MensaList(final List<Mensa> mensas) {
        this.mMensas = mensas;
    }
 
    public List<Mensa> getMensas() {
    	sortList();
        return mMensas;
    }
    
    public List<Mensa> getFavMensas() {
		List<Mensa> favmensas = new ArrayList<Mensa>();
		sortList();
		for (Mensa mensa : mMensas) {
			if (mensa.isFavorite()) {
				favmensas.add(mensa);
			}
		}
		
		if (favmensas.isEmpty()) {
			return null;
		} else {
			return favmensas;
		}
    }
    
	public Mensa getMensaById(int id) {
		for (Mensa mensa : mMensas) {
			if (mensa.getId() == id) {
				return mensa;
			}
		}
		return null;
	}
	
	public Mensa getMensaByLocation(LatLng location) {
		for (Mensa mensa : mMensas) {
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
	
	public void sortList() {
		Collections.sort(mMensas, new Comparator<Mensa>(){
		    public int compare(Mensa m1, Mensa m2) {
		    	// sorts the list that closer mensas are displayed first
		    	int result = m1.getDistance().compareTo(m2.getDistance());
		        return result;
		    }
		});
		
		Collections.sort(mMensas, new Comparator<Mensa>(){
		    public int compare(Mensa m1, Mensa m2) {
		    	// sorts the list that favorite mensas are displayed first
		    	int result = m2.isFavorite().compareTo(m1.isFavorite());
		        return result;
		    }
		});
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MensaList) {
			MensaList other = (MensaList) o;
			return this.getMensas().equals(other.getMensas());
		} else {
			return false;
		}
	}

}
