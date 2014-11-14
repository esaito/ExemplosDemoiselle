
package org.demoiselle.artigoJM.business;

import org.demoiselle.artigoJM.domain.Assinatura;
import org.demoiselle.artigoJM.persistence.AssinaturaDAO;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;


@BusinessController
public class AssinaturaBC extends DelegateCrud<Assinatura, Long, AssinaturaDAO> {
	private static final long serialVersionUID = 1L;
	
	
}
