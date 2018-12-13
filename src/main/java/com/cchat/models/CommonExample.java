package com.cchat.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonExample {

	@SerializedName("text")
	@Expose
	private String text;
	@SerializedName("intent")
	@Expose
	private String intent;
	@SerializedName("entities")
	@Expose
	private List<Entity> entities = null;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

}