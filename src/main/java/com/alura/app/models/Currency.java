package com.alura.app.models;

public record Currency(String code, String name, String country) {

	@Override
	public String toString() {
		return String.format("%s - %s, %s", code, name, country);
	}
}
