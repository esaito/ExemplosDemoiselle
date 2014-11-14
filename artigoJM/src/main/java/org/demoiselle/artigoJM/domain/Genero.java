package org.demoiselle.artigoJM.domain;

public enum Genero {
	
	MASCULINO("M"), FEMININO("F");

	private final String value;

	Genero(String value) {
		this.value = value;
	}

	public static Genero fromValue(String value) {
		if (value != null) {
			for (Genero sexo : values()) {
				if (sexo.value.equals(value)) {
					return sexo;
				}
			}
		}
		throw new IllegalArgumentException("Genero invalido: " + value);
	}

	public String toValue() {
		return value;
	}
}
