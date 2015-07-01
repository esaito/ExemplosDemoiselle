package br.gov.serpro.inscricao.business;

import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import br.gov.serpro.inscricao.entity.Aluno;
import br.gov.serpro.inscricao.persistence.AlunoDAO;

@BusinessController
public class AlunoBC extends DelegateCrud<Aluno, Integer, AlunoDAO>{
    private static final long serialVersionUID = 1L;
}