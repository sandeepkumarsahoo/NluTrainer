package com.cchat.models.existing;

import java.util.List;

import com.cchat.models.CommonExample;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RasaNluDataExisting {

	@SerializedName("common_examples")
	@Expose
	private List<CommonExample> CommonExampleExistings = null;
	@SerializedName("regex_features")
	@Expose
	private List<Object> regexFeatures = null;
	@SerializedName("lookup_tables")
	@Expose
	private List<Object> lookupTables = null;
	@SerializedName("entity_synonyms")
	@Expose
	private List<Object> entitySynonyms = null;

	public List<CommonExample> getCommonExampleExistings() {
		return CommonExampleExistings;
	}

	public void setCommonExampleExistings(List<CommonExample> CommonExampleExistings) {
		this.CommonExampleExistings = CommonExampleExistings;
	}

	public List<Object> getRegexFeatures() {
		return regexFeatures;
	}

	public void setRegexFeatures(List<Object> regexFeatures) {
		this.regexFeatures = regexFeatures;
	}

	public List<Object> getLookupTables() {
		return lookupTables;
	}

	public void setLookupTables(List<Object> lookupTables) {
		this.lookupTables = lookupTables;
	}

	public List<Object> getEntitySynonyms() {
		return entitySynonyms;
	}

	public void setEntitySynonyms(List<Object> entitySynonyms) {
		this.entitySynonyms = entitySynonyms;
	}

}