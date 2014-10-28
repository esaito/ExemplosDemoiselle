package br.org.frameworkdemoiselle.duasbases.view;

import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.org.frameworkdemoiselle.duasbases.business.TabelaBC;
import br.org.frameworkdemoiselle.duasbases.domain.Tabela;

@ViewController
@NextView("./tabela_edit.jsf")
@PreviousView("./tabela_list.jsf")
public class TabelaListMB extends AbstractListPageBean<Tabela, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private TabelaBC tabelaBC;
	
	@Override
	protected List<Tabela> handleResultList() {
		return this.tabelaBC.findAll();
	}
	
	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<Long> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			Long id = iter.next();
			delete = getSelection().get(id);
			if (delete) {
				tabelaBC.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}

}