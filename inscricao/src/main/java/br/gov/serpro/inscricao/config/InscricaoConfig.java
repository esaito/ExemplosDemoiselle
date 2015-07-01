package br.gov.serpro.inscricao.config;

import br.gov.frameworkdemoiselle.configuration.Configuration;

@Configuration(resource = "inscricao")
public class InscricaoConfig {

	private int capacidadeTurma;
	
	public int getCapacidadeTurma(){
		return capacidadeTurma;
	}
}
