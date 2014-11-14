package org.demoiselle.artigoJM.view;

import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import org.demoiselle.artigoJM.business.AssinanteBC;
import org.demoiselle.artigoJM.domain.Assinante;

@ViewController
@NextView("./assinante_edit.jsf")
@PreviousView("./assinante_list.jsf")
public class AssinanteListMB extends AbstractListPageBean<Assinante, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private AssinanteBC assinanteBC;
	
	@Override
	protected List<Assinante> handleResultList() {
		return this.assinanteBC.findAll();
	}
	
	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<Long> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			Long id = iter.next();
			delete = getSelection().get(id);
			if (delete) {
				assinanteBC.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}
}