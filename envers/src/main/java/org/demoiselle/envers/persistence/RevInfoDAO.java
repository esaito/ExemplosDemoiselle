package org.demoiselle.envers.persistence;

import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

import org.demoiselle.envers.domain.RevInfo;

@PersistenceController
public class RevInfoDAO extends JPACrud<RevInfo, Long> {

	private static final long serialVersionUID = 1L;
	

}
