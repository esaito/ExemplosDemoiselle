package br.org.frameworkdemoiselle.duasbases.business;

import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

import br.org.frameworkdemoiselle.duasbases.domain.Tabela;
import br.org.frameworkdemoiselle.duasbases.persistence.TabelaDAO;

@BusinessController
public class TabelaBC extends DelegateCrud<Tabela, Long, TabelaDAO> {
	
	private static final long serialVersionUID = 1L;
	
}
