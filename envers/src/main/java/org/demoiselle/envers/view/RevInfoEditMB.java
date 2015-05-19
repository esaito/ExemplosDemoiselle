
package org.demoiselle.envers.view;

import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import org.demoiselle.envers.business.*;
import org.demoiselle.envers.domain.*;
import javax.faces.model.*;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import java.util.*;

// To remove unused imports press: Ctrl+Shift+o

@ViewController
@PreviousView("./revInfo_list.jsf")
public class RevInfoEditMB extends AbstractEditPageBean<RevInfo, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private RevInfoBC revInfoBC;
	

	
	@Override
	@Transactional
	public String delete() {
		this.revInfoBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String insert() {
		this.revInfoBC.insert(this.getBean());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String update() {
		this.revInfoBC.update(this.getBean());
		return getPreviousView();
	}
	
	@Override
	protected RevInfo handleLoad(Long id) {
		return this.revInfoBC.load(id);
	}	
}