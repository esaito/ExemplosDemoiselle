
package org.demoiselle.artigoJM.view;

import java.util.List;
import javax.inject.Inject;
import org.demoiselle.artigoJM.business.AssinanteBC;
import org.demoiselle.artigoJM.business.AssinaturaBC;
import org.demoiselle.artigoJM.domain.Assinante;
import org.demoiselle.artigoJM.domain.Assinatura;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;


@ViewController
@PreviousView("./assinatura_list.jsf")
public class AssinaturaEditMB extends AbstractEditPageBean<Assinatura, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private AssinaturaBC assinaturaBC;
	

	@Inject
	private AssinanteBC assinanteBC;
	
	public List<Assinante> getAssinanteList(){
		return assinanteBC.findAll();
	}
			
	
	@Override
	@Transactional
	public String delete() {
		this.assinaturaBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String insert() {
		this.assinaturaBC.insert(this.getBean());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String update() {
		this.assinaturaBC.update(this.getBean());
		return getPreviousView();
	}
	
	@Override
	protected Assinatura handleLoad(Long id) {
		return this.assinaturaBC.load(id);
	}	
}