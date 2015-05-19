
package org.demoiselle.envers.business;

import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import org.demoiselle.envers.domain.*;
import java.util.*;
import javax.faces.model.*;
import org.demoiselle.envers.persistence.*;

// To remove unused imports press: Ctrl+Shift+o

@BusinessController
public class EstudanteAuditoriaBC extends DelegateCrud<EstudanteAuditoria, EstudanteAuditoriaID, EstudanteAuditoriaDAO> {
	private static final long serialVersionUID = 1L;
	
	
	public List<SelectItem> getGenero() {
		List<SelectItem> varGenero = new ArrayList<SelectItem>();
		for (Genero eachGenero : Genero.values()) {
			varGenero.add(new SelectItem(eachGenero));
		}
		return varGenero;
	}
	
}
