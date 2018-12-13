package com.cchat.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtility {
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static <T> String toJson(T requestObject) {
		return gson.toJson(requestObject);
	}

	public static <T> T fromJson(String json, Class<T> class1) {
		return gson.fromJson(json, class1);
	}

}
