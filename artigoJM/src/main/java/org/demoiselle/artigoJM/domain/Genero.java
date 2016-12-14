package org.demoiselle.artigoJM.domain;

public enum Genero {
	
	MASCULINO("Sexo Masculino"), FEMININO("Sexo Feminino"), OUTRO("Não definido") ;

	private String descricao;

	Genero(String descricao) {
		this.descricao = descricao;
	}

//	public static Genero fromValue(String descricao) {
//		if (descricao != null) {
//			for (Genero sexo : values()) {
//				if (sexo.descricao.equals(descricao)) {
//					return sexo;
//				}
//			}
//		}
//		throw new IllegalArgumentException("Genero invalido: " + descricao);
//	}


	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}


