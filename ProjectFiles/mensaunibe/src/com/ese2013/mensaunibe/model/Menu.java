package com.ese2013.mensaunibe.model;

import org.json.JSONObject;

/**
 * Class modeling a menu. Needs to be instantiated via the provided builder.
 * 
 * @author ese2013-team5
 * 
 */
public class Menu {

	private String title;
	private String date;
	private String price;
	private String[] menuDetails;
	private int mensaId;

	public Menu(MenuBuilder builder) {
		this.title = builder.title;
		this.date = builder.date;
		this.price = builder.price;
		this.menuDetails = builder.menuDetails;
		this.mensaId = builder.mensaId;
	}

	public String getTitle() {
		return title;
	}

	public String getDate() {
		return date;
	}

	public String getPrice() {
		return price;
	}

	public int getMensaId() {
		return mensaId;
	}

	public String[] getMenuDetails() {
		return menuDetails;
	}

	public static class MenuBuilder {
		private String title;
		private String date;
		private String price;
		private String[] menuDetails;
		private int mensaId;

		public MenuBuilder(JSONObject json) {
			parseJSONtoMenu(json);
		}

		private void parseJSONtoMenu(JSONObject json) {
			// TODO Parse JSON here to get a single menu
		}

		public MenuBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public MenuBuilder setDate(String date) {
			this.date = date;
			return this;
		}

		public MenuBuilder setPrice(String price) {
			this.price = price;
			return this;
		}

		public MenuBuilder setMenuDeteils(String[] menuDetails) {
			this.menuDetails = menuDetails;
			return this;
		}

		public MenuBuilder setMensaId(int mensaId) {
			this.mensaId = mensaId;
			return this;
		}

		public Menu build() {
			return new Menu(this);
		}
	}

}
