package br.gov.serpro.inscricao.persistence;

import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.serpro.inscricao.entity.Aluno;

@PersistenceController
public class AlunoDAO extends JPACrud<Aluno, Integer>{

	private static final long serialVersionUID = 1L;	
	
}
