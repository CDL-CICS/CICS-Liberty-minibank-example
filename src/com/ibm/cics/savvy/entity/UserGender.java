package com.ibm.cics.savvy.entity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Enum type for customer gender
 */
public enum UserGender {

	MALE {
		public String getDesc() {
			return "male";
		}
	},
	FEMALE {
		public String getDesc() {
			return "female";
		}
	};

	public static Map<String, String> toStringMap() {
		UserGender[] type = values();
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (type != null) {
			for (UserGender e : type) {
				map.put(e.toString(), e.getDesc());
			}
		}
		return map;
	}

	public abstract String getDesc();
}
