package br.org.frameworkdemoiselle.duasbases.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tabela implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String CampoUm;
	
	private String CampoDoise;
	
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCampoUm() {
		return CampoUm;
	}

	public void setCampoUm(String campoUm) {
		CampoUm = campoUm;
	}

	public String getCampoDoise() {
		return CampoDoise;
	}

	public void setCampoDoise(String campoDoise) {
		CampoDoise = campoDoise;
	}

	

}
