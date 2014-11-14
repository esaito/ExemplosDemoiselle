package org.demoiselle.artigoJM.view;

import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import org.demoiselle.artigoJM.business.AssinaturaBC;
import org.demoiselle.artigoJM.domain.Assinatura;

@ViewController
@NextView("./assinatura_edit.jsf")
@PreviousView("./assinatura_list.jsf")
public class AssinaturaListMB extends AbstractListPageBean<Assinatura, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private AssinaturaBC assinaturaBC;
	
	@Override
	protected List<Assinatura> handleResultList() {
		return this.assinaturaBC.findAll();
	}
	
	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<Long> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			Long id = iter.next();
			delete = getSelection().get(id);
			if (delete) {
				assinaturaBC.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}

}