package org.demoiselle.envers.view;

import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import org.demoiselle.envers.business.RevInfoBC;
import org.demoiselle.envers.domain.RevInfo;

@ViewController
@NextView("./revInfo_edit.jsf")
@PreviousView("./revInfo_list.jsf")
public class RevInfoListMB extends AbstractListPageBean<RevInfo, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private RevInfoBC revInfoBC;
	
	@Override
	protected List<RevInfo> handleResultList() {
		return this.revInfoBC.findAll();
	}
	
	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<Long> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			Long id = iter.next();
			delete = getSelection().get(id);
			if (delete) {
				revInfoBC.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}

}