
package org.demoiselle.envers.view;

import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import org.demoiselle.envers.business.*;
import org.demoiselle.envers.domain.*;
import javax.faces.model.*;
import java.util.*;

// To remove unused imports press: Ctrl+Shift+o

@ViewController
@PreviousView("./estudante_list.jsf")
public class EstudanteEditMB extends AbstractEditPageBean<Estudante, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EstudanteBC estudanteBC;
	

	public List<SelectItem> getGenero() {
		return estudanteBC.getGenero();
	}
	
	@Override
	@Transactional
	public String delete() {
		this.estudanteBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String insert() {
		this.estudanteBC.insert(this.getBean());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String update() {
		this.estudanteBC.update(this.getBean());
		return getPreviousView();
	}
	
	@Override
	protected Estudante handleLoad(Long id) {
		return this.estudanteBC.load(id);
	}	
}