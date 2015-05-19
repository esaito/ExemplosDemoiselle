package org.demoiselle.envers.persistence;

import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

import org.demoiselle.envers.domain.Estudante;

@PersistenceController
public class EstudanteDAO extends JPACrud<Estudante, Long> {

	private static final long serialVersionUID = 1L;
	

}
