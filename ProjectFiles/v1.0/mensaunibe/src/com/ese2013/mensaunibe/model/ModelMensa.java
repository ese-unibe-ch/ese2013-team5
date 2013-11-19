package com.ese2013.mensaunibe.model;

import java.util.ArrayList;
import java.util.Map;

import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.ese2013.mensaunibe.ActivityMain;
import com.ese2013.mensaunibe.util.BuilderMensa;
import com.google.gson.Gson;

/**
 * Class representing a mensa and responsible for its menus. Needs to be
 * instantiated via the provided builder.
 * 
 * @author ese2013-team5
 * 
 */
public class ModelMensa {

	private int id;
	private String name;
	private String address;
	private String city;
	private float lat;
	private float lon;
	private ModelWeeklyPlan weeklyPlan;
	private Map<String, Object> favMensaIds = ActivityMain.favMensaIds;

	public ModelMensa(BuilderMensa builder) {
		this.id = builder.getId();
		this.name = builder.getName();
		this.address = builder.getAddress();
		this.city = builder.getCity();
		this.lat = builder.getLat();
		this.lon = builder.getLon();
	}

	public ModelWeeklyPlan getWeeklyPlan() {
		return weeklyPlan;
	}

	public ArrayList<ModelMenu> getMenus(String day) {
		return weeklyPlan.getDailyPlan(day).getMenus();
	}
	
	public ArrayList<ModelMenu> getAllMenus(){
		ArrayList<ModelMenu> allMenus = new ArrayList<ModelMenu>();
		allMenus.addAll(getMenus("Monday"));
		allMenus.addAll(getMenus("Tuesday"));
		allMenus.addAll(getMenus("Wednesday"));
		allMenus.addAll(getMenus("Thursday"));
		allMenus.addAll(getMenus("Friday"));
		return allMenus;
	}

	public void setWeeklyPlan(ModelWeeklyPlan weeklyPlan) {
		this.weeklyPlan = weeklyPlan;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public boolean isFavorite() {
		//Toast.makeText(ActivityMain.getContext(), "favMensaids " + favMensaIds, Toast.LENGTH_SHORT).show();
		if ( favMensaIds.containsKey(String.valueOf(this.getId())) ) {
			//Toast.makeText(ActivityMain.getContext(), "Mensa " + this.getId() + " is favorite", Toast.LENGTH_SHORT).show();
			return true;
		} else {
			//Toast.makeText(ActivityMain.getContext(), "Mensa " + this.getId() + " is not favorite", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/**
	 * Sets whether this mensa is a favorite or not
	 * 
	 * @param isFavorite
	 *            true if favorite else false
	 * @return 
	 */
//	public void setFavorite(boolean isFavorite) {
	public void setFavorite(int mensaid) {
//		this.isFavorite = isFavorite;
		// the whole Map<> thing is just implemented like that
		// because it's easier and faster to check if a key is already there or not
		// than from a json object wherer we have much more exception traps...
		
		// get the shared prefs editor first
		Editor settingseditor = ActivityMain.settingseditor;
		// if the provided mensaid is not in the shared prefs, save it there
		if ( !this.isFavorite() ) {
			// modify the HashMap
			favMensaIds.put(String.valueOf(mensaid), true);
			// convert HashMap to JSON string for storage in prefs
			String favmensajsonstr = new Gson().toJson(favMensaIds);
			// put the new favorite mensas in shared prefs
        	settingseditor.putString("favmensas", favmensajsonstr);
		    settingseditor.commit();
		    Toast.makeText(ActivityMain.getContext(), "Mensa favorited", Toast.LENGTH_SHORT).show();
		} else {
			// if the provided mensaid is in the shared prefs and this method gets called
			// it means, that the user unfavorited the mensa
			// modify the HashMap
			favMensaIds.remove(String.valueOf(mensaid));
			// convert HashMap to JSON string for storage in prefs
			String favmensajsonstr = new Gson().toJson(favMensaIds);

        	settingseditor.putString("favmensas", favmensajsonstr);
		    settingseditor.commit();
			Toast.makeText(ActivityMain.getContext(), "Mensa unfavorited", Toast.LENGTH_SHORT).show();
		}
//		Model.getInstance().updateLocalData();
	}

}
