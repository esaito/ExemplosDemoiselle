
package org.demoiselle.envers.view;

import java.util.List;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.demoiselle.envers.business.EstudanteAuditoriaBC;
import org.demoiselle.envers.domain.EstudanteAuditoria;
import org.demoiselle.envers.domain.EstudanteAuditoriaID;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;

// To remove unused imports press: Ctrl+Shift+o

@ViewController
@PreviousView("./estudanteAuditoria_list.jsf")
public class EstudanteAuditoriaEditMB extends AbstractEditPageBean<EstudanteAuditoria, EstudanteAuditoriaID> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EstudanteAuditoriaBC estudanteAuditoriaBC;
	

	public List<SelectItem> getGenero() {
		return estudanteAuditoriaBC.getGenero();
	}
	
	@Override
	@Transactional
	public String delete() {
		this.estudanteAuditoriaBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String insert() {
		this.estudanteAuditoriaBC.insert(this.getBean());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String update() {
		this.estudanteAuditoriaBC.update(this.getBean());
		return getPreviousView();
	}
	
	@Override
	protected EstudanteAuditoria handleLoad(EstudanteAuditoriaID id) {
		return this.estudanteAuditoriaBC.load(id);
	}}