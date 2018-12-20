package com.rasa.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RasaNluData {

	@SerializedName("common_examples")
	@Expose
	private List<CommonExample> commonExamples = null;

	public List<CommonExample> getCommonExamples() {
		return commonExamples;
	}

	public void setCommonExamples(List<CommonExample> commonExamples) {
		this.commonExamples = commonExamples;
	}

}

