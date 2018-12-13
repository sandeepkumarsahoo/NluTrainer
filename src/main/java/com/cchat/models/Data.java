package com.cchat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

	@SerializedName("rasa_nlu_data")
	@Expose
	private RasaNluData rasaNluData;

	public RasaNluData getRasaNluData() {
		return rasaNluData;
	}

	public void setRasaNluData(RasaNluData rasaNluData) {
		this.rasaNluData = rasaNluData;
	}

}