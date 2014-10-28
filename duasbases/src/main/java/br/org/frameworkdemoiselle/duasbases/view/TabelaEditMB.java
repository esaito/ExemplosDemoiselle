package br.org.frameworkdemoiselle.duasbases.view;

import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.org.frameworkdemoiselle.duasbases.business.TabelaBC;
import br.org.frameworkdemoiselle.duasbases.domain.Tabela;

@ViewController
@PreviousView("./tabela_list.jsf")
public class TabelaEditMB extends AbstractEditPageBean<Tabela, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private TabelaBC tabelaBC;
	
	@Override
	@Transactional
	public String delete() {
		this.tabelaBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String insert() {
		this.tabelaBC.insert(getBean());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String update() {
		this.tabelaBC.update(getBean());
		return getPreviousView();
	}
	
	@Override
	protected void handleLoad() {
		setBean(this.tabelaBC.load(getId()));
	}

}