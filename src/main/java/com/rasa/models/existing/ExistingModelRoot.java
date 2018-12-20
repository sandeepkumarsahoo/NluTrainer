package com.rasa.models.existing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExistingModelRoot {

	@SerializedName("rasa_nlu_data")
	@Expose
	private RasaNluDataExisting RasaNluDataExisting;

	public RasaNluDataExisting getRasaNluDataExisting() {
		return RasaNluDataExisting;
	}

	public void setRasaNluDataExisting(RasaNluDataExisting RasaNluDataExisting) {
		this.RasaNluDataExisting = RasaNluDataExisting;
	}

}