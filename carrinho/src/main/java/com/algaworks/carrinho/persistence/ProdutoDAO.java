package com.algaworks.carrinho.persistence;

import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

import com.algaworks.carrinho.domain.Produto;

@PersistenceController
public class ProdutoDAO extends JPACrud<Produto, Integer> {

	private static final long serialVersionUID = 1L;
	

}
