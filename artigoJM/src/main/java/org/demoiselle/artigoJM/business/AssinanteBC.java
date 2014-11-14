package org.demoiselle.artigoJM.business;

import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import org.demoiselle.artigoJM.domain.*;
import java.util.*;
import javax.faces.model.*;
import org.demoiselle.artigoJM.persistence.AssinanteDAO;

@BusinessController
public class AssinanteBC extends DelegateCrud<Assinante, Long, AssinanteDAO> {
	private static final long serialVersionUID = 1L;
	
	public List<SelectItem> getGenero() {
		List<SelectItem> varGenero = new ArrayList<SelectItem>();
		for (Genero eachGenero : Genero.values()) {
			varGenero.add(new SelectItem(eachGenero));
		}
		return varGenero;
	}	
}