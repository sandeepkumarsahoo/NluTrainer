package com.cchat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainRequest {

	@SerializedName("language")
	@Expose
	private String language;
	@SerializedName("pipeline")
	@Expose
	private String pipeline;
	@SerializedName("data")
	@Expose
	private Data data;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPipeline() {
		return pipeline;
	}

	public void setPipeline(String pipeline) {
		this.pipeline = pipeline;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}


}