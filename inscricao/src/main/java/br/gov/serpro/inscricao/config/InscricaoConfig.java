package br.gov.serpro.inscricao.config;

import br.gov.frameworkdemoiselle.annotation.Name;
import br.gov.frameworkdemoiselle.configuration.Configuration;

@Configuration(resource = "inscricao")
public class InscricaoConfig {

	@Name(value="capacidade.turma")
	private int capacidadeTurma;
	
	public int getCapacidadeTurma(){
		return capacidadeTurma;
	}
}
