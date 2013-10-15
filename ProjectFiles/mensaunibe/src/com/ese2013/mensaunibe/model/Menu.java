package com.ese2013.mensaunibe.model;

import java.util.Date;

public class Menu {
	
	private String title;
	private String date;
	private String price;
	private String[] menuDetails;
	private int mensaId;
	
	public Menu(String tTitle, String tDate, String tPrice, String[] tMenuDetails, int tMensaId){
		title = tTitle;
		menuDetails = tMenuDetails;
		date = tDate;
		price = tPrice;
		mensaId = tMensaId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getMensaId() {
		return mensaId;
	}
	public void setMensaId(int mensaId) {
		this.mensaId = mensaId;
	}

	public String[] getMenuDetails() {
		return menuDetails;
	}

	public void setMenuDetails(String[] menuDetails) {
		this.menuDetails = menuDetails;
	}

}
