package org.demoiselle.artigoJM.view;

import java.util.List;
import javax.faces.model.*;
import javax.inject.Inject;
import org.demoiselle.artigoJM.business.AssinanteBC;
import org.demoiselle.artigoJM.domain.*;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;


@ViewController
@PreviousView("./assinante_list.jsf")
public class AssinanteEditMB extends AbstractEditPageBean<Assinante, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private AssinanteBC assinanteBC;
	

	public List<SelectItem> getGenero() {
		return assinanteBC.getGenero();
	}
	
	private DataModel<Assinatura> assinaturaList = new ListDataModel<Assinatura>();
	
	public void addAssinatura() {
		this.getBean().getAssinaturas().add(new Assinatura());
	}
	public void deleteAssinatura() {
	   this.getBean().getAssinaturas().remove(getAssinaturaList().getRowData());
	}
	public DataModel<Assinatura> getAssinaturaList() {
	   if (!assinaturaList.isRowAvailable()) {
		   assinaturaList = new ListDataModel<Assinatura>(this.getBean().getAssinaturas());
	   }
	   return assinaturaList;
	} 
	
	@Override
	@Transactional
	public String delete() {
		this.assinanteBC.delete(getId());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String insert() {
		this.assinanteBC.insert(this.getBean());
		return getPreviousView();
	}
	
	@Override
	@Transactional
	public String update() {
		this.setBean(this.assinanteBC.update(this.getBean()));
		return getPreviousView();
	}
	
	@Override
	protected Assinante handleLoad(Long id) {
		return this.assinanteBC.load(id);
	}	
}