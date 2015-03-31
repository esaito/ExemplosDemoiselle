
package com.algaworks.carrinho.view;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import com.algaworks.carrinho.business.ProdutoBC;
import com.algaworks.carrinho.domain.Produto;

@ViewController
@PreviousView("./produto_list.jsf")
public class ProdutoEditMB extends AbstractEditPageBean<Produto, Integer> {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoBC produtoBC;
	

	
	@Override
	@Transactional
	public String delete() {
		this.produtoBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String insert() {
		this.produtoBC.insert(this.getBean());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String update() {
		this.produtoBC.update(this.getBean());
		return getPreviousView();
	}
	
	@Override
	protected Produto handleLoad(Integer id) {
		return this.produtoBC.load(id);
	}	
}