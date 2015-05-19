package org.demoiselle.envers.view;

import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import org.demoiselle.envers.business.EstudanteBC;
import org.demoiselle.envers.domain.Estudante;

@ViewController
@NextView("./estudante_edit.jsf")
@PreviousView("./estudante_list.jsf")
public class EstudanteListMB extends AbstractListPageBean<Estudante, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EstudanteBC estudanteBC;
	
	@Override
	protected List<Estudante> handleResultList() {
		return this.estudanteBC.findAll();
	}
	
	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<Long> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			Long id = iter.next();
			delete = getSelection().get(id);
			if (delete) {
				estudanteBC.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}

}