package com.rasa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entity {

	@SerializedName("start")
	@Expose
	private Integer start;
	@SerializedName("end")
	@Expose
	private Integer end;
	@SerializedName("value")
	@Expose
	private String value;
	@SerializedName("entity")
	@Expose
	private String entity;

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

}