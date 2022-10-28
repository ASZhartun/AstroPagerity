package com.example.astropagerity;

import java.util.HashMap;

public class MoonPhaseParameterItem extends HashMap<String, String> {
	public static final String NAME = "name";
	public static final String VALUE = "value";
	
	public MoonPhaseParameterItem(String name, String value) {
		super();
		this.put(NAME, name);
		this.put(VALUE, value);
	}

}
