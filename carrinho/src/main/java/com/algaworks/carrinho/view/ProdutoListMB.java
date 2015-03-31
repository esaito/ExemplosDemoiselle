package com.algaworks.carrinho.view;

import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import com.algaworks.carrinho.business.ProdutoBC;
import com.algaworks.carrinho.domain.Produto;

@ViewController
@NextView("./produto_edit.jsf")
@PreviousView("./produto_list.jsf")
public class ProdutoListMB extends AbstractListPageBean<Produto, Integer> {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoBC produtoBC;
	
	@Override
	protected List<Produto> handleResultList() {
		return this.produtoBC.findAll();
	}
	
	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<Integer> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			Integer id = iter.next();
			delete = getSelection().get(id);
			if (delete) {
				produtoBC.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}

}