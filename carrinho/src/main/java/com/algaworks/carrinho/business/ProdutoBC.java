
package com.algaworks.carrinho.business;

import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

import com.algaworks.carrinho.domain.Produto;
import com.algaworks.carrinho.persistence.ProdutoDAO;


@BusinessController
public class ProdutoBC extends DelegateCrud<Produto, Integer, ProdutoDAO> {
	private static final long serialVersionUID = 1L;
	
	
}
