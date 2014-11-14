package org.demoiselle.artigoJM.persistence;

import org.demoiselle.artigoJM.domain.Assinante;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class AssinanteDAO extends JPACrud<Assinante, Long> {

	private static final long serialVersionUID = 1L;
	
}
