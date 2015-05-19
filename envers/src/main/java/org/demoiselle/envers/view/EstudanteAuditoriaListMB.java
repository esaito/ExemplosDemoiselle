package org.demoiselle.envers.view;

import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import org.demoiselle.envers.business.EstudanteAuditoriaBC;
import org.demoiselle.envers.domain.EstudanteAuditoria;
import org.demoiselle.envers.domain.EstudanteAuditoriaID;

@ViewController
@NextView("./estudanteAuditoria_edit.jsf")
@PreviousView("./estudanteAuditoria_list.jsf")
public class EstudanteAuditoriaListMB extends AbstractListPageBean<EstudanteAuditoria, EstudanteAuditoriaID> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EstudanteAuditoriaBC estudanteAuditoriaBC;
	
	@Override
	protected List<EstudanteAuditoria> handleResultList() {
		return this.estudanteAuditoriaBC.findAll();
	}
	
	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<EstudanteAuditoriaID> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			EstudanteAuditoriaID id = iter.next();
			delete = getSelection().get(id);
			if (delete) {
				estudanteAuditoriaBC.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}

}