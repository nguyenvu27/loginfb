package com.goplay.gamesdk.models;

public enum OpenIdType {

	FACEBOOK(0), GOOGLE(1);

	private final Integer value;

	OpenIdType(Integer value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
